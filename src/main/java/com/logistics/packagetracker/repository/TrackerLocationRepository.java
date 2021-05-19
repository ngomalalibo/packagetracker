package com.logistics.packagetracker.repository;

import com.logistics.packagetracker.entity.TrackingLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackerLocationRepository extends MongoRepository<TrackingLocation, String>
{
}
