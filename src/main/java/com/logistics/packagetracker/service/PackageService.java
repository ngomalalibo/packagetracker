package com.logistics.packagetracker.service;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
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
    
    String trackPackage(TrackingDetails track, String id);
    
    // tracking starts with pickup
    Package createPackage(Package entity);
    
    List<Package> findByStatusAndTrackingCode(PackageStatus status, String code);
    
    List<Package> findByStatus(PackageStatus status);
    
}
