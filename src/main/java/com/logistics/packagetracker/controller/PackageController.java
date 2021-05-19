package com.logistics.packagetracker.controller;

import com.logistics.packagetracker.dto.PackageDTO;
import com.logistics.packagetracker.dto.PackageMapper;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/package")
public class PackageController
{
    @Autowired
    PackageMapper packageMapper;
    
    @Autowired
    PackageService packageService;
    
    public PackageController(PackageService packageService)
    {
        this.packageService = packageService;
    }
    
    @GetMapping("/getPackages")
    public ResponseEntity<List<PackageDTO>> getAllPackages()
    {
        return ResponseEntity.ok(packageService.findAllPackages().stream().map(s -> packageMapper.convertToDto(s)).collect(Collectors.toList()));
    }
    
    @GetMapping("/getPackage/{id}")
    public ResponseEntity<PackageDTO> getPackage(@PathVariable String id)
    {
        return ResponseEntity.ok(packageMapper.convertToDto(packageService.getPackageById(id)));
    }
    
    @PostMapping("/pickupPackage")
    public ResponseEntity pickUp(@RequestBody PackageDTO packageDTO)
    {
        packageService.pickUpPackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PutMapping("/sendPackage/{id}")
    public ResponseEntity sendPackage(@RequestBody PackageDTO packageDTO)
    {
        packageService.sendPackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/storePackage/{id}")
    public ResponseEntity storePackage(@RequestBody PackageDTO packageDTO)
    {
        packageService.storePackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/deliverPackage/{id}")
    public ResponseEntity deliverPackage(@RequestBody PackageDTO packageDTO)
    {
        packageService.deliverPackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/cancel/{id}")
    public ResponseEntity cancelOrder(@PathVariable String id)
    {
        packageService.cancelOrderById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @GetMapping("/getTrackerByStatus/{status}")
    public ResponseEntity<PackageDTO> getPackage(@PathVariable PackageStatus status)
    {
        return ResponseEntity.ok(packageMapper.convertToDto(packageService.findByStatus(status)));
    }
    
}
