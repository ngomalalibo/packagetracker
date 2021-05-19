package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

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
    private PackageStatus status;
    private String createdDate;
    private double weight;
    private String estDeliveryDate;
    private String carrier;
    public boolean isCancelled;
    
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
