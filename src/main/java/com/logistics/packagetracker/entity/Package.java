package com.logistics.packagetracker.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.serviceimpl.PackageServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Package
{
    /**
     * Annotate primary key for mongo collection
     */
    @BsonProperty("_id")
    @JsonProperty("_id")
    private String id;
    private String trackingCode;
    private PackageStatus status;
    private String createdDate;
    private double weight;
    private String estDeliveryDate;
    
    List<TrackingDetail> trackingDetails = new ArrayList<>();
    
    public Package(String trackingCode, PackageStatus status, String createdDate, double weight, String estDeliveryDate, List<TrackingDetail> trackingDetails)
    {
        this.trackingCode = trackingCode;
        this.status = status;
        this.createdDate = createdDate;
        this.weight = weight;
        this.estDeliveryDate = estDeliveryDate;
        this.trackingDetails = trackingDetails;
    }
    
    public boolean equals(Package pack)
    {
        PackageService packageService = new PackageServiceImpl();
        TrackingDetail thisTracker = packageService.getCurrentTracker(this.id);
        TrackingDetail packTracker = packageService.getCurrentTracker(pack.id);
        return this.id.equals(pack.id) &&
                this.trackingCode.equals(pack.trackingCode) &&
                this.weight == pack.weight &&
                thisTracker.getStatus().equals(packTracker.getStatus()) &&
                thisTracker.getSource().equals(packTracker.getSource()) &&
                thisTracker.getCity().equals(packTracker.getCity()) &&
                thisTracker.getState().equals(packTracker.getState()) &&
                thisTracker.getCountry().equals(packTracker.getCountry()) &&
                thisTracker.getZip().equals(packTracker.getZip());
    }
    
}
