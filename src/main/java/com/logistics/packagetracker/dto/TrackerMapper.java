package com.logistics.packagetracker.dto;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.PackageDTO;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TrackerMapper
{
    @Autowired
    PackageService packageService;
    
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
