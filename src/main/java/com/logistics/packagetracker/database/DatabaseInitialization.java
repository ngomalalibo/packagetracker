package com.logistics.packagetracker.database;

import com.github.javafaker.Faker;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import com.logistics.packagetracker.util.GenerateTrackingCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Locale;

@Slf4j
@Configuration
public class DatabaseInitialization
{
    private final Faker faker = new Faker(Locale.getDefault());
    
    
    @Autowired
    private PackageService packageService;
    
    @Autowired
    MongoTemplate mongoTemplate;
    
    private Package pack;
    private TrackingDetail trackingDetail;
    
    @Bean
    CommandLineRunner initDatabase()
    {
        return args ->
        {
            /** used to initialize database with data during development*/
            /*log.info("Initializing database");
            trackingDetail = initTrackerDetails();
            pack = initPackage();
            log.info("Initialization completed");*/
        };
    }
    
    public TrackingDetail initTrackerDetails()
    {
        return new TrackingDetail(PackageStatus.PICKED_UP, System.currentTimeMillis(), faker.company().name(), faker.address().city(), faker.address().state(), faker.address().country(), faker.address().zipCode());
    }
    
    public Package initPackage()
    {
        
        Package pack = new Package(GenerateTrackingCode.generateTrackingCode(), PackageStatus.PICKED_UP, pickupDate(), 16.7, deliveryDate(), Collections.singletonList(trackingDetail));
        return packageService.createPackage(pack);
    }
    
    public String pickupDate() // stamp package with pickup date
    {
        return ZonedDateTime.now().format(DateConverter.formatter);
    }
    
    public String deliveryDate()// stamp package with pickup date
    {
        return ZonedDateTime.now().plusDays(5).format(DateConverter.formatter);
    }
    
}
