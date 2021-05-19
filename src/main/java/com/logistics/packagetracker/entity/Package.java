package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.util.DateConverter;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    private String createdDate;
    private double weight;
    private String estDeliveryDate;
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
}
