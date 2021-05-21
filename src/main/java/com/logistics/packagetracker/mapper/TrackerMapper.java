package com.logistics.packagetracker.mapper;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.PackageDTO;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manually map our TrackingDetail object to and from the PackageDTO for user consumption. THis was used in place of a model mapper library due to the time constraints
 * and also the requirement of package tracker API.
 * */

@Component
public class TrackerMapper
{
    @Autowired
    private PackageService packageService;
    
    public PackageDTO convertToDto(Package pack)
    {
        TrackingDetail currentTracker = packageService.getCurrentTracker(pack.getId());
        return new PackageDTO(pack.getId(), currentTracker.getStatus(), currentTracker.getSource(), currentTracker.getCity(),
                              currentTracker.getState(), currentTracker.getCountry(), currentTracker.getZip());
    }
    
    
    public TrackingDetail convertToEntity(PackageDTO dto)
    {
        return new TrackingDetail(dto.getStatus(), System.currentTimeMillis(), dto.getCurrentSource(),
                              dto.getCurrentCity(), dto.getCurrentState(),
                              dto.getCurrentCountry(), dto.getCurrentZipcode());
    }
}
