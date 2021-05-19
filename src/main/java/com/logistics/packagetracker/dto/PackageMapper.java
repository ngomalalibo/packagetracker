package com.logistics.packagetracker.dto;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.repository.PackageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PackageMapper
{
    @Autowired
    ModelMapper modelMapper;
    
    @Autowired
    PackageRepository packageRepository;
    
    public PackageDTO convertToDto(Package aPackage)
    {
        return modelMapper.map(aPackage, PackageDTO.class);
        
    }
    
    
    public Package convertToEntity(PackageDTO packageDTO)
    {
        Package aPackage = modelMapper.map(packageDTO, Package.class);
        
        packageRepository.findById(packageDTO.getId()).ifPresent(packageById -> packageById.setCurrentTracker(
                new TrackingDetails(packageDTO.getStatus(), LocalDateTime.now(), packageDTO.getCurrentSource(),
                                    packageDTO.getCurrentCity(), packageDTO.getCurrentState(),
                                    packageDTO.getCurrentCountry(), packageDTO.getCurrentZipcode())));
        
        return aPackage;
    }
}
