package com.logistics.packagetracker.service;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.entity.TrackingDetailDTO;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/** This is the interface specification for the Package Tracking operation. */
@Service
public interface PackageService
{
    List<Package> findAllPackages();
    
    Package getPackageById(String id);
    
    boolean existsById(String id);
    
    long count();
    
    String trackPackage(TrackingDetail track, String id);
    
    Package createPackage(Package entity);
    
    boolean statusExistsInPackageHistory(String id, PackageStatus status);
    
    List<Package> findByStatus(String status);
    
    TrackingDetail getCurrentTracker(String id);
    
    List<TrackingDetailDTO> getPackageTrackingHistory(String id);
    
}
