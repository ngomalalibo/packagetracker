package com.logistics.packagetracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@OpenAPIDefinition(info = @Info(title = "Package Tracker API Documentation",
        version = "v0.0.1",
        description = "For faster delivery and better service.",
        license = @io.swagger.v3.oas.annotations.info.License(name = "Package Tracker",
                url = "http://www.google.com")))
@Slf4j
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.logistics.packagetracker.repository")
public class PackageTrackerApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder)
    {
        return builder.sources(PackageTrackerApplication.class);
    }
    
    public static void main(String[] args)
    {
        SpringApplication.run(PackageTrackerApplication.class, args);
    }
}
