package com.logistics.packagetracker.service;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PackageService
{
    List<Package> findAllPackages();
    
    Package getPackageById(String id);
    
    boolean existsById(String id);
    
    long count();
    
    String trackPackage(TrackingDetail track, String id);
    
    Package createPackage(Package entity);
    
    boolean isPickedUp(String id);
    
    boolean isDelivered(String id);
    
    List<Package> findByStatus(PackageStatus status);
    
    TrackingDetail getCurrentTracker(String id);
    
}
