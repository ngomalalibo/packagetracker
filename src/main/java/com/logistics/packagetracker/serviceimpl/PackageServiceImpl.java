package com.logistics.packagetracker.serviceimpl;

import com.google.common.base.Strings;
import com.logistics.packagetracker.aspect.Loggable;
import com.logistics.packagetracker.dataProvider.PackageDataRepository;
import com.logistics.packagetracker.database.MongoConnection;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityNotFoundException;
import com.logistics.packagetracker.repository.PackageRepository;
import com.logistics.packagetracker.service.PackageService;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService
{
   @Autowired
    private PackageRepository packageRepository;
    
    @Autowired
    PackageDataRepository packageDataRepository;
    
    @Autowired
    MongoConnection mongoConnection;
    
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
            throw new EntityNotFoundException("Provide a valid package ID");
        }
        return packageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Provide a valid package ID: " + id));
    }
    
    @Override
    public boolean existsById(String id)
    {
        if (Strings.isNullOrEmpty(id))
        {
            throw new EntityNotFoundException("Provide a valid package ID");
        }
        return packageRepository.existsById(id);
    }
    
    @Override
    public long count()
    {
        return packageRepository.count();
    }
    
    @Override
    public String trackPackage(TrackingDetails track, String id)
    {
        if (track == null)
        {
            Bson filter = Filters.eq("_id", id);
            Bson match1 = Aggregates.match(filter);
            Bson updArray = Updates.push("trackingDetails", track);
            List<Bson> pipeline = List.of(match1, updArray);
            AggregateIterable<Package> aggregate = mongoConnection.tracker.aggregate(pipeline);
            if (aggregate.iterator().hasNext())
            {
                return id;
            }
            else
            {
                throw new EntityNotFoundException("Could not update entity with ID: " + id);
            }
        }
        else
        {
            throw new EntityNotFoundException("Provide package to track");
        }
    }
    
    
    @Loggable
    @Override
    public Package createPackage(Package entity)
    {
        Package saved = packageRepository.save(entity);
        
        if (packageRepository.findById(saved.getId()).isPresent())
        {
            return saved;
        }
        return null;
    }
    
    @Override
    public List<Package> findByStatusAndTrackingCode(PackageStatus status, String code)
    {
        return packageRepository.findByStatusAndTrackingCode(status, code).orElseThrow(() -> new EntityNotFoundException("Could not find package with status: " + status));
    }
    
    @Override
    public List<Package> findByStatus(PackageStatus status)
    {
        return packageRepository.findByStatus(status).orElseThrow(() -> new EntityNotFoundException("No package with status: " + status));
    }
}
