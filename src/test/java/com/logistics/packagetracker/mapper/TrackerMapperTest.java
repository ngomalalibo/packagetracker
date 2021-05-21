package com.logistics.packagetracker.mapper;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetailDTO;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Calendar;
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
        Package pack = packageService.getPackageById("60a7e70e13cd056c5c844ba8");
        TrackingDetail packTracker = packageService.getCurrentTracker(pack.getId());
        TrackingDetailDTO dto = trackerMapper.convertToDto(packTracker, pack.getId() );
        assertEquals(pack.getId(), dto.getId());
        assertEquals(packTracker.getStatus(), dto.getStatus());
        assertEquals(packTracker.getCity(), dto.getCurrentCity());
        assertEquals(packTracker.getState(), dto.getCurrentState());
        assertEquals(packTracker.getCountry(), dto.getCurrentCountry());
        assertEquals(packTracker.getZip(), dto.getCurrentZipcode());
    }
    
    @Test
    void convertToEntity()
    {
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        TrackingDetailDTO dto = new TrackingDetailDTO("60a6a40862b3066832617f50", PackageStatus.PICKED_UP, dateTime, "UPS", "Ikeja", "Lagos", "Nigeria", "100001");
        TrackingDetail thisTracker = trackerMapper.convertToEntity(dto);
        
        assertEquals(dto.getStatus(), thisTracker.getStatus());
        assertEquals(dto.getCurrentSource(), thisTracker.getSource());
        assertEquals(dto.getCurrentCity(), thisTracker.getCity());
        assertEquals(dto.getCurrentState(), thisTracker.getState());
        assertEquals(dto.getCurrentCountry(), thisTracker.getCountry());
        assertEquals(dto.getCurrentZipcode(), thisTracker.getZip());
    }
}