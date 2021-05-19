package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.util.DateToLocalDateTimeConverter;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
@Audited
@Document(collection = "tracker")
public class Package
{
    
    @org.springframework.data.annotation.Id
    private String Id;
    @Indexed(unique = true)
    private String trackingCode;
    private LocalDateTime createdDate;
    private double weight;
    private LocalDateTime estDeliveryDate;
    private String carrier;
    private TrackingDetails currentTracker;
    
    List<TrackingDetails> trackingDetails;
    
    public static String generateTrackingCode()
    {
        
        String raw = UUID.randomUUID().toString();
        String[] sect = raw.split("-");
        StringBuilder dd = new StringBuilder();
        for (int i = 0; i < sect.length; i++)
        {
            dd.append(sect[i].charAt(2));
        }
        return dd.toString().toUpperCase();
    }
    
    public void setCreatedDate(String date)
    {
        this.createdDate = DateToLocalDateTimeConverter.stringToLocalDateTime(date);
    }
    
    public void setEstDeliveryDate(String date)
    {
        this.estDeliveryDate = DateToLocalDateTimeConverter.stringToLocalDateTime(date);
    }
    
    public void setWeight(String weight)
    {
        this.weight = Double.parseDouble(weight);
    }
}
