package com.logistics.packagetracker.service;

import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;

import java.util.List;

public interface TrackerDetailsService
{
    List<TrackingDetails> findAll();
    
    TrackingDetails findById(String id);
    
    TrackingDetails save(TrackingDetails entity);
    
    TrackingDetails update(TrackingDetails entity);
    
    boolean existsById(String var1);
    
    long count();
    
    TrackingDetails findByTrackingCode(String code, PackageStatus status);
    
    TrackingDetails findByStatus(PackageStatus status);
    
    
}
