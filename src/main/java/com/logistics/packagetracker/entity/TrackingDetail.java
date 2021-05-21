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
public class TrackingDetail
{
    private PackageStatus status;
    private Long dateTime;
    private String source;
    private String city;
    private String state;
    private String country;
    private String zip;
    
    public TrackingDetail(PackageStatus status, Long dateTime, String source, String city, String state, String country, String zip)
    {
        this.status = status;
        this.dateTime = dateTime;
        this.source = source;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zip = zip;
    }
    
    public boolean equals(TrackingDetail dt)
    {
        return this.city.equals(dt.city) && this.state.equals(dt.state) && this.country.equals(dt.country) && this.status.equals(dt.status) && this.source.equals(dt.source);
    }
}
