package com.logistics.packagetracker.dto;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PackageMapperTest
{
    @Autowired
    ModelMapper modelMapper;
    
    @Test
    void convertToDto()
    {
        TrackingDetails trackingDetails = new TrackingDetails(PackageStatus.IN_TRANSIT, LocalDateTime.now(), "Fedex",
                                                              "Ikeja", "Lagos", "Nigeria", "100001");
        Package aPackage = new Package("5", "ABC45",
                                       PackageStatus.PICKED_UP, LocalDateTime.now(),
                                       16.7, LocalDateTime.now(), "UPS", trackingDetails, Collections.singletonList(trackingDetails));
        
        PackageDTO packageDTO = modelMapper.map(aPackage, PackageDTO.class);
        assertEquals(aPackage.getId() , packageDTO.getId());
        assertEquals(aPackage.getTrackingCode() , packageDTO.getTrackingCode());
        assertEquals(aPackage.getStatus() , packageDTO.getId());
        assertEquals(aPackage.getId() , packageDTO.getId());
        assertEquals(aPackage.getId() , packageDTO.getId());
        assertEquals(aPackage.getId() , packageDTO.getId());
        assertEquals(aPackage.getId() , packageDTO.getId());
        assertEquals(aPackage.getId() , packageDTO.getId());
        assertEquals(aPackage.getId() , packageDTO.getId());
        
    }
    
    @Test
    void convertToEntity()
    {
    }
}