package com.logistics.packagetracker;

import com.logistics.packagetracker.database.MongoConnection;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@OpenAPIDefinition(info = @Info(title = "Package Tracker API Documentation",
        version = "v0.0.1",
        description = "For faster delivery and better service.",
        license = @io.swagger.v3.oas.annotations.info.License(name = "Package Tracker",
                url = "http://www.google.com")))
@Slf4j
@SpringBootApplication
public class PackagetrackerApplication extends SpringBootServletInitializer
{
    /**
     * A logistics company has contracted you to build a service that will be used to track packages. Possible package statuses are PICKED_UP, IN_TRANSIT, WAREHOUSE, DELIVERED.
     * It’s possible for a package to be IN_TRANSIT, and WAREHOUSE multiple times but it’s not possible to be PICKED_UP and DELIVERED more than once.
     * <p>
     * Build a service to cater this requirement as started by the logistics company. You are free to decide what endpoints your service will have and how they will work.
     */
    
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder)
    {
        return builder.sources(PackagetrackerApplication.class);
    }
    
    public static void main(String[] args)
    {
        SpringApplication.run(PackagetrackerApplication.class, args);
    }
    
    @EventListener(classes = {ApplicationReadyEvent.class})
    public void handleMultipleEvents()
    {
        log.info("Application is Ready");
    }
}
