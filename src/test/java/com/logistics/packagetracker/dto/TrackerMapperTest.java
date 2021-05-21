package com.logistics.packagetracker.dto;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.PackageDTO;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class TrackerMapperTest
{
    @Autowired
    TrackerMapper trackerMapper;
    
    @Autowired
    PackageService packageService;
    
    @BeforeEach
    public void setup()
    {
    }
    
    @Test
    void convertToDto()
    {
        TrackingDetail trackingDetail = new TrackingDetail(PackageStatus.PICKED_UP, System.currentTimeMillis(), "Fedex",
                                                           "Ikeja", "Lagos", "Nigeria", "100001");
        Package pack = new Package("ABC45", PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter), 16.7, ZonedDateTime.now().format(DateConverter.formatter), Collections.singletonList(trackingDetail));
        pack.setId(new ObjectId().toHexString());
        pack = packageService.createPackage(pack);
        TrackingDetail packTracker = packageService.getCurrentTracker(pack.getId());
        PackageDTO dto = trackerMapper.convertToDto(pack);
        assertEquals(pack.getId(), dto.getId());
        assertEquals(pack.getStatus(), dto.getStatus());
        assertEquals(packTracker.getCity(), dto.getCurrentCity());
        assertEquals(packTracker.getState(), dto.getCurrentState());
        assertEquals(packTracker.getCountry(), dto.getCurrentCountry());
        assertEquals(packTracker.getZip(), dto.getCurrentZipcode());
    }
    
    @Test
    void convertToEntity()
    {
        PackageDTO dto = new PackageDTO("60a6a40862b3066832617f50", PackageStatus.PICKED_UP, "UPS", "Ikeja", "Lagos", "Nigeria", "100001");
        TrackingDetail thisTracker = trackerMapper.convertToEntity(dto);
        
        assertEquals(dto.getStatus(), thisTracker.getStatus());
        assertEquals(dto.getCurrentSource(), thisTracker.getSource());
        assertEquals(dto.getCurrentCity(), thisTracker.getCity());
        assertEquals(dto.getCurrentState(), thisTracker.getState());
        assertEquals(dto.getCurrentCountry(), thisTracker.getCountry());
        assertEquals(dto.getCurrentZipcode(), thisTracker.getZip());
    }
}