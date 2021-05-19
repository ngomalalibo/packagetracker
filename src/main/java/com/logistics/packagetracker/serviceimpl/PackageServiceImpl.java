package com.logistics.packagetracker.serviceimpl;

import com.google.common.base.Strings;
import com.logistics.packagetracker.aspect.Loggable;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityException;
import com.logistics.packagetracker.repository.PackageRepository;
import com.logistics.packagetracker.service.PackageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PackageServiceImpl implements PackageService
{
    @Autowired
    ModelMapper modelMapper;
    
    @Autowired
    private PackageRepository packageRepository;
    
    @Override
    public List<Package> findAllPackages()
    {
        return packageRepository.findAll();
    }
    
    @Override
    public Package getPackageById(String id)
    {
        if (Strings.isNullOrEmpty(id))
        {
            throw new EntityException("Provide a valid package ID");
        }
        return packageRepository.findById(id).orElseThrow(() -> new EntityException("Provide a valid package ID: " + id));
    }
    
    @Override
    public Package updatePackage(Package entity)
    {
        if (Objects.isNull(entity) || Strings.isNullOrEmpty(entity.getId()))
        {
            throw new EntityException("Provide valid package");
        }
        packageRepository.findById(entity.getId()).orElseThrow(() -> new EntityException("Package not found"));
        return packageRepository.save(entity);
    }
    
    @Override
    public boolean existsById(String id)
    {
        if (Strings.isNullOrEmpty(id))
        {
            throw new EntityException("Provide a valid package ID");
        }
        return packageRepository.existsById(id);
    }
    
    @Override
    public long count()
    {
        return packageRepository.count();
    }
    
    @Loggable
    @Override
    public void pickUpPackage(Package entity)
    {
        if (Objects.isNull(entity) || Strings.isNullOrEmpty(entity.getId()))
        {
            throw new EntityException("Provide valid package");
        }
        
        if (entity.getCurrentTracker().getStatus().equals(PackageStatus.PICKED_UP))
        {
            Package retrievedTracker = packageRepository.findByTrackingCode(entity.getTrackingCode(), PackageStatus.PICKED_UP);
            if (retrievedTracker != null)
            {
                throw new EntityException("Package is already being tracked. Cannot perform another pick up on package. ");
            }
            packageRepository.save(entity);
        }
        else
        {
            throw new EntityException("Check entity status. Entity status should be picked up");
        }
    }
    
    @Loggable
    @Override
    public void sendPackage(Package aPackage)
    {
        Package returned = packageRepository.findById(aPackage.getId()).orElseThrow(() -> new EntityException("Could not find entity with ID: " + aPackage.getId()));
        aPackage.getCurrentTracker().setStatus(PackageStatus.IN_TRANSIT);
        returned.setCurrentTracker(aPackage.getCurrentTracker());
        returned.getTrackingDetails().add(aPackage.getCurrentTracker());
        packageRepository.save(returned);
    }
    
    @Loggable
    @Override
    public void storePackage(Package aPackage)
    {
        Package returned = packageRepository.findById(aPackage.getId()).orElseThrow(() -> new EntityException("Could not find package with ID: " + aPackage.getId()));
        aPackage.getCurrentTracker().setStatus(PackageStatus.WAREHOUSE);
        returned.setCurrentTracker(aPackage.getCurrentTracker());
        returned.getTrackingDetails().add(aPackage.getCurrentTracker());
        packageRepository.save(returned);
    }
    
    @Loggable
    @Override
    public void deliverPackage(Package aPackage)
    {
        Package returned = packageRepository.findById(aPackage.getId()).orElseThrow(() -> new EntityException("Could not find package with ID: " + aPackage.getId()));
        
        Package retrievedTracker = packageRepository.findByTrackingCode(aPackage.getTrackingCode(), PackageStatus.PICKED_UP);
        if (retrievedTracker != null)
        {
            throw new EntityException("Package is already being tracked. Cannot perform another pick up on package. ");
        }
        aPackage.getCurrentTracker().setStatus(PackageStatus.DELIVERED);
        returned.setCurrentTracker(aPackage.getCurrentTracker());
        returned.getTrackingDetails().add(aPackage.getCurrentTracker());
        packageRepository.save(returned);
    }
    
    @Loggable
    @Override
    public PackageStatus cancelOrderById(String id)
    {
        Package returned = packageRepository.findById(id).orElseThrow(() -> new EntityException("Could not find package with ID: " + id));
        returned.getCurrentTracker().setStatus(PackageStatus.CANCELLED);
        returned.getTrackingDetails().add(returned.getCurrentTracker());
        packageRepository.save(returned);
        return PackageStatus.WAREHOUSE;
    }
    
    @Override
    public Package findByStatus(PackageStatus status)
    {
        return packageRepository.findByStatus(status).orElseThrow(() -> new EntityException("Could not find package with status: " + status));
    }
    
    @Override
    public Package findByTrackingDetailsStatus(PackageStatus status)
    {
        return packageRepository.findByStatus(status).orElseThrow(() -> new EntityException("Could not find package with status: " + status));
    }
    
    @Override
    public Package findByTrackingCode(String code, PackageStatus status)
    {
        return packageRepository.findByTrackingCode(code, status);
    }
    
    
}
