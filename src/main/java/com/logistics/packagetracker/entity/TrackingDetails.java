package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrackingDetails
{
    private PackageStatus status;
    private String dateTime;
    private String source;
    private String city;
    private String state;
    private String country;
    private String zip;
}
