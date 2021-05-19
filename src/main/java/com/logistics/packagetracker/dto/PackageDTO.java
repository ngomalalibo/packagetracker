package com.logistics.packagetracker.dto;

import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.util.DateToLocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PackageDTO
{
    private String id;
    private String trackingCode;
    private PackageStatus status;
    private String createdDate;
    private String weight;
    private String carrier;
    private String estDeliveryDate;
    private String currentSource;
    private String currentCity;
    private String currentState;
    private String currentCountry;
    private String currentZipcode;
    
    public void setCreatedDate(LocalDateTime createdDate)
    {
        this.createdDate = DateToLocalDateTimeConverter.localDateTimeToString(createdDate);
    }
    
    public void setEstDeliveryDate(LocalDateTime estDeliveryDate)
    {
        this.estDeliveryDate = DateToLocalDateTimeConverter.localDateTimeToString(estDeliveryDate);
    }
    
    public void setWeight(double weight)
    {
        this.weight = String.valueOf(weight);
    }
    
}
