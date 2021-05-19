package com.logistics.packagetracker.dto;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.PackageDTO;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.util.DateConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageMapperTest
{
    @Autowired
    PackageMapper packageMapper;
    
    @Test
    void convertToDto()
    {
        TrackingDetails trackingDetails = new TrackingDetails(PackageStatus.IN_TRANSIT, ZonedDateTime.now().format(DateConverter.formatter), "Fedex",
                                                              "Ikeja", "Lagos", "Nigeria", "100001");
        Package pack = new Package("5", "ABC45", ZonedDateTime.now().format(DateConverter.formatter), 16.7, ZonedDateTime.now().format(DateConverter.formatter), trackingDetails, Collections.singletonList(trackingDetails));
        
        PackageDTO dto = packageMapper.convertToDto(pack);
        assertEquals(pack.getId(), dto.getId());
        assertEquals(pack.getTrackingCode(), dto.getTrackingCode());
        assertEquals(pack.getCurrentTracker().getStatus(), dto.getStatus());
        assertEquals(pack.getCurrentTracker().getCity(), dto.getCurrentCity());
        assertEquals(pack.getCurrentTracker().getState(), dto.getCurrentState());
        assertEquals(pack.getCurrentTracker().getCountry(), dto.getCurrentCountry());
        assertEquals(pack.getCurrentTracker().getZip(), dto.getCurrentZipcode());
    }
    
    @Test
    void convertToEntity()
    {
        PackageDTO dto = new PackageDTO("5", "ABC45", PackageStatus.PICKED_UP, "UPS", "Ikeja", "Lagos", "Nigeria", "100001");
        Package pack = packageMapper.convertToEntity(dto);
        
        assertEquals(dto.getId(), pack.getId());
        assertEquals(dto.getTrackingCode(), pack.getTrackingCode());
        assertEquals(dto.getStatus(), pack.getCurrentTracker().getStatus());
        assertEquals(dto.getCurrentSource(), pack.getCurrentTracker().getSource());
        assertEquals(dto.getCurrentCity(), pack.getCurrentTracker().getCity());
        assertEquals(dto.getCurrentState(), pack.getCurrentTracker().getState());
        assertEquals(dto.getCurrentCountry(), pack.getCurrentTracker().getCountry());
        assertEquals(dto.getCurrentZipcode(), pack.getCurrentTracker().getZip());
    }
}