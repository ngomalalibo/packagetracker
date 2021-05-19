package com.logistics.packagetracker.dto;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.PackageDTO;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.exception.EntityNotFoundException;
import com.logistics.packagetracker.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PackageMapper
{
    @Autowired
    PackageRepository packageRepository;
    
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy HH:mm:ss a Z");
    
    public PackageDTO convertToDto(Package pack)
    {
        return new PackageDTO(pack.getId(), pack.getTrackingCode(), pack.getCurrentTracker().getStatus(), pack.getCurrentTracker().getSource(), pack.getCurrentTracker().getCity(),
                              pack.getCurrentTracker().getState(), pack.getCurrentTracker().getCountry(), pack.getCurrentTracker().getZip());
        
    }
    
    
    public Package convertToEntity(PackageDTO dto)
    {
        Package pack = packageRepository.findById(dto.getId()).orElse(null);
        if (pack != null)
        {
            TrackingDetails tracker = new TrackingDetails(dto.getStatus(), ZonedDateTime.now().format(formatter), dto.getCurrentSource(),
                                                          dto.getCurrentCity(), dto.getCurrentState(),
                                                          dto.getCurrentCountry(), dto.getCurrentZipcode());
            pack.setCurrentTracker(tracker);
            return pack;
        }
        else
        {
            throw new EntityNotFoundException("Unable convert DTO to package");
        }
    }
    
    
}
