package com.logistics.packagetracker.service;

import com.logistics.packagetracker.entity.Package;
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
    PackageStatus pickUp(Package entity);
    
    PackageStatus sendPackage(String id);
    
    PackageStatus storePackage(String id);
    
    PackageStatus deliverPackage(String id);
    
    PackageStatus cancelOrderById(String id);
    
    Package findByStatus(PackageStatus status);
    
}
