package com.logistics.packagetracker.service;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;

import java.util.List;

public interface PackageService
{
    List<Package> findAllPackages();
    
    Package getPackageById(String id);
    
    Package updatePackage(Package entity);
    
    boolean existsById(String id);
    
    long count();
    
    // tracking starts with pickup
    void pickUpPackage(Package entity);
    
    void sendPackage(Package entity);
    
    void storePackage(Package entity);
    
    void deliverPackage(Package entity);
    
    PackageStatus cancelOrderById(String id);
    
    Package findByStatus(PackageStatus status);
    
    Package findByTrackingCode(String code, PackageStatus status);
    
    Package findByTrackingDetailsStatus(PackageStatus status);
    
}
