package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TrackingDetailsDTO
{
    private String id;
    private PackageStatus status;
    private String currentSource;
    private String currentCity;
    private String currentState;
    private String currentCountry;
    private String currentZipcode;
    
    public TrackingDetailsDTO(String id, PackageStatus status, String currentSource, String currentCity, String currentState, String currentCountry, String currentZipcode)
    {
        this.id = id;
        this.status = status;
        this.currentSource = currentSource;
        this.currentCity = currentCity;
        this.currentState = currentState;
        this.currentCountry = currentCountry;
        this.currentZipcode = currentZipcode;
    }
}
