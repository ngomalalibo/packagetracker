package com.logistics.packagetracker.database;

import com.github.javafaker.Faker;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.service.TrackerDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.util.Locale;

@Slf4j
@Configuration
public class DatabaseInitialization
{
    private final Faker faker = new Faker(Locale.getDefault());
    
    @Autowired
    PackageService packageService;
    
    @Autowired
    TrackerDetailsService trackerDetailsService;
    
    private Package aPackage;
    private TrackingDetails trackingDetails;
    
    @Bean
    CommandLineRunner initDatabase()
    {
        return args ->
        {
            log.info("Initializing database");
            trackingDetails = initTrackerDetails();
            aPackage = initTrackers();
            
            log.info("Initialization completed");
        };
    }
    
    private TrackingDetails initTrackerDetails()
    {
        new TrackingDetails(null, aPackage.getTrackingCode(), PackageStatus.PICKED_UP, pickupDate(), faker.company().name(), faker.address().city(), faker.address().state(), faker.address().country(), faker.address().zipCode());
        return null;
    }
    
    private Package initTrackers()
    {
        new Package(null, Package.generateTrackingCode(), PackageStatus.PICKED_UP, pickupDate(), 16.7, deliveryDate(), "UPS", false);
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
