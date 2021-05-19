package com.logistics.packagetracker.repository;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends MongoRepository<Package, String>
{
    
    @Query("{'status':?0}")
    Optional<Package> findByStatus(PackageStatus status);
    
    
    Package findByTrackingDetailsStatus(PackageStatus status);
    
    @Query("{'trackingCode':?0, 'status':?1}")
    Package findByTrackingCode(String code, PackageStatus status);
    
    
}
