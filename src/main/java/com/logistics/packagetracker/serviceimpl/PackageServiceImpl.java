package com.logistics.packagetracker.serviceimpl;

import com.google.common.base.Strings;
import com.logistics.packagetracker.aspect.Loggable;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityException;
import com.logistics.packagetracker.repository.PackageRepository;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.service.TrackerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PackageServiceImpl implements PackageService
{
    @Autowired
    private PackageRepository packageRepository;
    
    @Autowired
    private TrackerDetailsService trackerDetailsService;
    
    public PackageServiceImpl(PackageRepository packageRepository)
    {
        this.packageRepository = packageRepository;
    }
    
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
    public PackageStatus pickUp(Package entity)
    {
        if (Objects.isNull(entity) || Strings.isNullOrEmpty(entity.getId()))
        {
            throw new EntityException("Provide valid package");
        }
        
        if (entity.getStatus().equals(PackageStatus.PICKED_UP))
        {
            TrackingDetails retrievedTracker = trackerDetailsService.findByTrackingCode(entity.getTrackingCode(), PackageStatus.PICKED_UP);
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
        return PackageStatus.PICKED_UP;
    }
    
    @Loggable
    @Override
    public PackageStatus sendPackage(String id)
    {
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new EntityException("Could not find entity with ID: " + id));
        aPackage.setStatus(PackageStatus.IN_TRANSIT);
        packageRepository.save(aPackage);
        // Tracker detail is implemented at the end of this method by the @Loggable aspect
        return PackageStatus.IN_TRANSIT;
    }
    
    @Loggable
    @Override
    public PackageStatus storePackage(String id)
    {
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new EntityException("Could not find package with ID: " + id));
        aPackage.setStatus(PackageStatus.WAREHOUSE);
        packageRepository.save(aPackage);
        return PackageStatus.WAREHOUSE;
    }
    
    @Loggable
    @Override
    public PackageStatus deliverPackage(String id)
    {
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new EntityException("Could not find package with ID: " + id));
        aPackage.setStatus(PackageStatus.DELIVERED);
        
        TrackingDetails retrievedTracker = trackerDetailsService.findByTrackingCode(aPackage.getTrackingCode(), PackageStatus.PICKED_UP);
        if (retrievedTracker != null)
        {
            throw new EntityException("Package is already being tracked. Cannot perform another pick up on package. ");
        }
        packageRepository.save(aPackage);
        return PackageStatus.WAREHOUSE;
    }
    
    @Loggable
    @Override
    public PackageStatus cancelOrderById(String id)
    {
        Package aPackage = packageRepository.findById(id).orElseThrow(() -> new EntityException("Could not find package with ID: " + id));
        aPackage.setStatus(PackageStatus.CANCELLED);
        packageRepository.save(aPackage);
        return PackageStatus.WAREHOUSE;
    }
    
    @Override
    public Package findByStatus(PackageStatus status)
    {
        return packageRepository.findByStatus(status).orElseThrow(() -> new EntityException("Could not find package with status: " + status));
    }
}
