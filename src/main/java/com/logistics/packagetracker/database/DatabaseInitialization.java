package com.logistics.packagetracker.database;

import com.github.javafaker.Faker;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Locale;

@Slf4j
@Configuration
public class DatabaseInitialization
{
    private final Faker faker = new Faker(Locale.getDefault());
    
    @Autowired
    PackageService packageService;
    
    private Package aPackage;
    private TrackingDetails trackingDetails;
    
    @Bean
    CommandLineRunner initDatabase()
    {
        return args ->
        {
            log.info("Initializing database");
            trackingDetails = initTrackerDetails();
            aPackage = initPackage();
            
            log.info("Initialization completed");
        };
    }
    
    private TrackingDetails initTrackerDetails()
    {
        return new TrackingDetails(PackageStatus.PICKED_UP, pickupDate(), faker.company().name(), faker.address().city(), faker.address().state(), faker.address().country(), faker.address().zipCode());
    }
    
    private Package initPackage()
    {
        new Package(null, Package.generateTrackingCode(), PackageStatus.PICKED_UP, pickupDate(), 16.7, deliveryDate(), "UPS", false, Collections.singletonList(trackingDetails));
        return null;
    }
    
    public String pickupDate()
    {
        return ZonedDateTime.now().toString();
    }
    
    public String deliveryDate()
    {
        return ZonedDateTime.now().plusDays(5).toString();
    }
}
