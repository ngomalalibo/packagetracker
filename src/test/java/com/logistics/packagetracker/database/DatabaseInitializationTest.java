package com.logistics.packagetracker.database;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DatabaseInitializationTest
{
    
    @Autowired
    PackageService packageService;
    @Autowired
    DatabaseInitialization dataInit;
    
    @BeforeEach
    public void setup()
    {
        dataInit = new DatabaseInitialization();
    }
    
    
    @Test
    void initDatabase()
    {
        // Arrange // Act // Assert
        
        TrackingDetail trackingDetail = new TrackingDetail(PackageStatus.IN_TRANSIT, System.currentTimeMillis(), "Fedex",
                                                           "Ikeja", "Lagos", "Nigeria", "100001");
        Package expected = new Package("ABC45",PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter), 16.7, ZonedDateTime.now().format(DateConverter.formatter), Collections.singletonList(trackingDetail));
        new DatabaseInitialization().initDatabase();
        Package actual = packageService.getPackageById(expected.getId());
        
        assertNotNull(actual);
        assertTrue(actual.equals(expected));
        
        
    }
}