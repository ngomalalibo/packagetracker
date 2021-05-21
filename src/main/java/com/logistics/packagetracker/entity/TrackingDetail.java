package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.stereotype.Component;

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
    
    public TrackingDetail()
    {
    }
    
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
    
    public PackageStatus getStatus()
    {
        return status;
    }
    
    public void setStatus(PackageStatus status)
    {
        this.status = status;
    }
    
    public Long getDateTime()
    {
        return dateTime;
    }
    
    public void setDateTime(Long dateTime)
    {
        this.dateTime = dateTime;
    }
    
    public String getSource()
    {
        return source;
    }
    
    public void setSource(String source)
    {
        this.source = source;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getState()
    {
        return state;
    }
    
    public void setState(String state)
    {
        this.state = state;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getZip()
    {
        return zip;
    }
    
    public void setZip(String zip)
    {
        this.zip = zip;
    }
    
    public boolean equals(TrackingDetail dt)
    {
        return this.city.equals(dt.city) && this.state.equals(dt.state) && this.country.equals(dt.country) && this.status.equals(dt.status) && this.source.equals(dt.source);
    }
}
