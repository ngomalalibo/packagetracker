package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "trackerdetail")
public class TrackingDetails
{
    @org.springframework.data.annotation.Id
    private String Id;
    private String trackingCode;
    private PackageStatus status;
    private String dateTime;
    private String source;
    private TrackingLocation trackingLocation;
}
