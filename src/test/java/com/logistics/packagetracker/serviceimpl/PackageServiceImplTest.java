package com.logistics.packagetracker.serviceimpl;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class PackageServiceImplTest
{
    @Autowired
    PackageService packageService;
    
    @BeforeEach
    public void setup()
    {
        
    }
    
    @Test
    void findAllPackages()
    {
        long expected = packageService.count();
        long actual = packageService.findAllPackages().size();
        
        assertEquals(expected, actual);
    }
    
    @Test
    void getPackageById()
    {
        String id = "60a6a40862b3066832617f50";
        Package actual = packageService.getPackageById(id);
        
        assertEquals(id, actual.getId());
    }
    
    @Test
    void existsById()
    {
        String id = "60a6a40862b3066832617f50";
        packageService.existsById(id);
        assertTrue(packageService.existsById(id));
    }
    
    @Test
    void count()
    {
        long expected = packageService.findAllPackages().size();
        long actual = packageService.count();
        
        assertEquals(expected, actual);
    }
    
    @Disabled
    @Test
    void trackPackage()
    {
        String id = "60a6a40862b3066832617f50";
        long datetime = System.currentTimeMillis();
        TrackingDetail expected = new TrackingDetail(PackageStatus.IN_TRANSIT, datetime, "inSource", "inCity", "inState", "inCountry", "inZip");
        String s = packageService.trackPackage(expected, id);
        TrackingDetail actual = packageService.getCurrentTracker(s);
        
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getZip(), actual.getZip());
    }
    
    @Test
    void isPickedUpOrDelivered()
    {
        String id = "60a6d03bbd41d20bcbd60d28";
        boolean expected = packageService.isPickedUp(id);
        assertTrue(expected);
        
        id = "60a6a40862b3066832617f50";
        expected = packageService.isDelivered(id);
        assertTrue(expected);
    }
    
    @Test
    void findByStatus()
    {
        PackageStatus ps = PackageStatus.PICKED_UP;
        long expected = packageService.count();
        
        long actual = packageService.findByStatus(ps.toString()).size()+2;
        assertEquals(expected, actual);
        
        ps = PackageStatus.DELIVERED;
        expected = 1;
        
        actual = packageService.findByStatus(ps.toString()).size();
        assertEquals(expected, actual);
        
        
    }
    
    @Disabled
    @Test
    void createPackage()
    {
        Package expected = new Package("TA34I", PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter),
                                       30.9, ZonedDateTime.now().plusDays(6).format(DateConverter.formatter), List.of(new TrackingDetail()));
        Package actual = packageService.createPackage(expected);
        assertEquals(expected.getTrackingCode(), actual.getTrackingCode());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        assertEquals(expected.getWeight(), actual.getWeight());
        assertEquals(expected.getEstDeliveryDate(), actual.getEstDeliveryDate());
        assertEquals(expected.getTrackingDetails().size(), actual.getTrackingDetails().size());
    }
    
    
}