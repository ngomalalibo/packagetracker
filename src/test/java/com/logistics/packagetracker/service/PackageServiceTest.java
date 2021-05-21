package com.logistics.packagetracker.service;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.util.DateConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackageServiceTest
{
    @Mock
    PackageService packageService;
    
    // Arrange // Act // Assert
    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @AfterEach
    public void tearDown()
    {
    
    }
    
    @Test
    void findAllPackages()
    {
        Package expected = new Package("TA34I", PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter),
                                       30.9, ZonedDateTime.now().plusDays(6).format(DateConverter.formatter), List.of(new TrackingDetail()));
        Mockito.when(packageService.findAllPackages()).thenReturn(List.of(expected));
        
        List<Package> actual = packageService.findAllPackages();
        assertEquals(expected.getTrackingCode(), actual.get(0).getTrackingCode());
    }
    
    
    @Test
    void getPackageById()
    {
        String id = "60a6a40862b3066832617f50";
        Package expected = new Package("TA34I", PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter),
                                       30.9, ZonedDateTime.now().plusDays(6).format(DateConverter.formatter), List.of(new TrackingDetail()));
        Mockito.when(packageService.getPackageById(id)).thenReturn(expected);
        Package actual = packageService.getPackageById(id);
        assertEquals(expected.getStatus(), actual.getStatus());
    }
    
    @Test
    void existsById()
    {
        boolean expected = false;
        String id = "60a6a40862b3066832617f50";
        Mockito.when(packageService.existsById(id)).thenReturn(expected);
        
        boolean actual = packageService.existsById(id);
        assertFalse(actual);
        Mockito.verify(packageService, Mockito.times(1)).existsById(ArgumentMatchers.anyString());
    }
    
    @Test
    void count()
    {
        long expected = 5;
        Mockito.when(packageService.count()).thenReturn(expected);
        long actual = packageService.count();
        assertEquals(expected, actual);
    }
    
    @Test
    void trackPackage()
    {
        String expected = "60a6a40862b3066832617f50";
        long datetime = System.currentTimeMillis();
        TrackingDetail prop = new TrackingDetail(PackageStatus.IN_TRANSIT, datetime, "inSource", "inCity", "inState", "inCountry", "inZip");
        Mockito.when(packageService.trackPackage(prop, expected)).thenReturn(expected);
        
        String actual = packageService.trackPackage(prop, expected);
        assertEquals(expected, actual);
    }
    
    @Test
    void createPackage()
    {
        Package expected = new Package("TA34I", PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter),
                                       30.9, ZonedDateTime.now().plusDays(6).format(DateConverter.formatter), List.of(new TrackingDetail()));
        Mockito.when(packageService.createPackage(expected)).thenReturn(expected);
        Package actual = packageService.createPackage(expected);
        
        assertEquals(expected.getStatus(), actual.getStatus());
        Mockito.verify(packageService, Mockito.atLeastOnce()).createPackage(Mockito.any());
    }
    
    @Test
    void isPickedUpAndDelivered()
    {
        String id = "60a6a1f04472cf6fade46233";
        boolean expected = true;
        
        Mockito.when(packageService.isPickedUp(id)).thenReturn(expected);
        boolean actual = packageService.isPickedUp(id);
        assertTrue(actual);
        expected = false;
        Mockito.when(packageService.isDelivered(id)).thenReturn(expected);
        expected = packageService.isDelivered(id);
        assertFalse(expected);
        Mockito.verify(packageService, Mockito.times(1)).isDelivered(id);
        
    }
    
    @Test
    void findByStatus()
    {
        PackageStatus ps = PackageStatus.PICKED_UP;
        Package expected = new Package("TA34I", PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter),
                                       30.9, ZonedDateTime.now().plusDays(6).format(DateConverter.formatter), List.of(new TrackingDetail()));
        
        
        Mockito.when(packageService.findByStatus(ps.toString())).thenReturn(List.of(expected));
        List<Package> actual = packageService.findByStatus(ps.toString());
        assertEquals(expected.getTrackingCode(), actual.get(0).getTrackingCode());
    }
    
    @Test
    void getCurrentTracker()
    {
        String id = "60a6a1f04472cf6fade46233";
        long dateTime = System.currentTimeMillis();
        TrackingDetail expected = new TrackingDetail(PackageStatus.IN_TRANSIT, dateTime, "inSource", "inCity", "inState", "inCountry", "inZip");
        Mockito.when(packageService.getCurrentTracker(id)).thenReturn(expected);
        
        TrackingDetail actual = packageService.getCurrentTracker(id);
        assertEquals(expected, actual);  // using overridden equals method defined in TrackingDetail class
    }
}