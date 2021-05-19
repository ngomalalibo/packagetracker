package com.logistics.packagetracker.database;

import com.github.javafaker.Faker;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;

@Slf4j
@Configuration
public class DatabaseInitialization
{
    private final Faker faker = new Faker(Locale.getDefault());
    
    
    @Autowired
    PackageService packageService;
    
    private Package pack;
    private TrackingDetails trackingDetails;
    
    @Bean
    CommandLineRunner initDatabase()
    {
        return args ->
        {
            log.info("Initializing database");
            trackingDetails = initTrackerDetails();
            pack = initPackage();
            
            log.info("Initialization completed");
        };
    }
    
    private TrackingDetails initTrackerDetails()
    {
        return new TrackingDetails(PackageStatus.PICKED_UP, pickupDate(), faker.company().name(), faker.address().city(), faker.address().state(), faker.address().country(), faker.address().zipCode());
    }
    
    private Package initPackage()
    {
        
        return new Package(null, Package.generateTrackingCode(), pickupDate(), 16.7, deliveryDate(), trackingDetails, Collections.singletonList(trackingDetails));
    }
    
    public String pickupDate()
    {
        return ZonedDateTime.now().format(DateConverter.formatter);
    }
    
    public String deliveryDate()
    {
        return ZonedDateTime.now().plusDays(5).format(DateConverter.formatter);
    }
}
