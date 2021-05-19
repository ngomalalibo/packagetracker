package com.logistics.packagetracker.repository;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

public interface PackageRepository extends RevisionRepository<Package, String, Integer>, MongoRepository<Package, String>
{
    
    @Query("{'status':?0}")
    Optional<Package> findByStatus(PackageStatus status);
    
    
    Package findByTrackingDetailsStatus(PackageStatus status);
    
    @Query("{'trackingCode':?0, 'status':?1}")
    Package findByTrackingCode(String code, PackageStatus status);
    
    
}
