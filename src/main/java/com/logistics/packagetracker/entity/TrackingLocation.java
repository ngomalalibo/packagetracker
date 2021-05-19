package com.logistics.packagetracker.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "trackerlocation")
public class TrackingLocation
{
    @org.springframework.data.annotation.Id
    private String Id;
    private String city;
    private String state;
    private String country;
    private String zip;
}
