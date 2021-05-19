package com.logistics.packagetracker.repository;

import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TrackerDetailsRepository extends MongoRepository<TrackingDetails, String>
{
    TrackingDetails findByStatus(PackageStatus status);
    
    @Query("{'trackingCode':?0, 'status':?1}")
    TrackingDetails findByTrackingCode(String code, PackageStatus status);
}
