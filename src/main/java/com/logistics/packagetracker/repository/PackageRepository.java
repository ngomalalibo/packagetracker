package com.logistics.packagetracker.repository;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends MongoRepository<Package, String>
{
    @Query("{'status':?0}")
    Optional<List<Package>> findByStatus(PackageStatus status);
    
    @Query("{'trackingCode':?0, 'status':?1}")
    Optional<List<Package>> findByStatusAndTrackingCode(PackageStatus status, String trackingCode);
    
    
    
    
}
