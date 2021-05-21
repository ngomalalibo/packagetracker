package com.logistics.packagetracker.mapper;

import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.entity.TrackingDetailDTO;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manually map our TrackingDetail object to and from the PackageDTO for user consumption. THis was used in place of a model mapper library due to the time constraints
 * and also the requirement of package tracker API.
 */

@Component
public class TrackerMapper
{
    @Autowired
    private PackageService packageService;
    
    public TrackingDetailDTO convertToDto(TrackingDetail pack, String id)
    {
        
        return new TrackingDetailDTO(id, pack.getStatus(), DateConverter.millisecToTimestampConv(pack.getDateTime()), pack.getSource(), pack.getCity(),
                                     pack.getState(), pack.getCountry(), pack.getZip());
    }
    
    
    public TrackingDetail convertToEntity(TrackingDetailDTO dto)
    {
        return new TrackingDetail(dto.getStatus(), dto.getDateTime().getTime(), dto.getCurrentSource(),
                                  dto.getCurrentCity(), dto.getCurrentState(),
                                  dto.getCurrentCountry(), dto.getCurrentZipcode());
    }
}
