package com.logistics.packagetracker.controller;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/package")
public class PackageController
{
    @Autowired
    PackageService packageService;
    
    public PackageController(PackageService packageService)
    {
        this.packageService = packageService;
    }
    
    /*
   
    vvoid cancelOrderById(String id);
    
    Tracker findByStatus(TrackerStatus status);*/
    @GetMapping("/getPackages")
    public ResponseEntity<List<Package>> getAllPackages()
    {
        return ResponseEntity.ok(packageService.findAllPackages());
    }
    
    @GetMapping("/getPackage/{id}")
    public ResponseEntity<Package> getPackage(@PathVariable String id)
    {
        return ResponseEntity.ok(packageService.getPackageById(id));
    }
    
    @PostMapping("/pickupPackage")
    public ResponseEntity pickUp(@RequestBody Package aPackage)
    {
        packageService.pickUp(aPackage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PutMapping("/sendPackage/{id}")
    public ResponseEntity sendPackage(@PathVariable String id)
    {
        packageService.sendPackage(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/storePackage/{id}")
    public ResponseEntity storePackage(@PathVariable String id)
    {
        packageService.storePackage(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/deliverPackage/{id}")
    public ResponseEntity deliverPackage(@PathVariable String id)
    {
        packageService.deliverPackage(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/cancel/{id}")
    public ResponseEntity cancelOrder(@PathVariable String id)
    {
        packageService.cancelOrderById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @GetMapping("/getTrackerByStatus/{status}")
    public ResponseEntity<Package> getPackage(@PathVariable PackageStatus status)
    {
        return ResponseEntity.ok(packageService.findByStatus(status));
    }
    
}
