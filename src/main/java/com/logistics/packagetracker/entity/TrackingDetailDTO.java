package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TrackingDetailDTO
{
    private String id;
    private PackageStatus status;
    Timestamp dateTime;
    private String currentSource;
    private String currentCity;
    private String currentState;
    private String currentCountry;
    private String currentZipcode;
    
    public TrackingDetailDTO(String id, PackageStatus status, Timestamp dateTime, String currentSource, String currentCity, String currentState, String currentCountry, String currentZipcode)
    {
        this.id = id;
        this.status = status;
        this.dateTime = dateTime;
        this.currentSource = currentSource;
        this.currentCity = currentCity;
        this.currentState = currentState;
        this.currentCountry = currentCountry;
        this.currentZipcode = currentZipcode;
    }
}
