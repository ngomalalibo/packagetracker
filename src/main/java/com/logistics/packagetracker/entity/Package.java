package com.logistics.packagetracker.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "packages")
public class Package
{
    /**
     * Annotate primary key for mongo collection
     */
    @BsonProperty("_id")
    @JsonProperty("_id")
    @Id
    private String id;
    private String trackingCode;
    private PackageStatus status;
    private String createdDate;
    private double weight;
    private String estDeliveryDate;
    
    private List<TrackingDetail> trackingDetails = new ArrayList<>();
    
    public Package(String trackingCode, PackageStatus status, String createdDate, double weight, String estDeliveryDate, List<TrackingDetail> trackingDetails)
    {
        this.trackingCode = trackingCode;
        this.status = status;
        this.createdDate = createdDate;
        this.weight = weight;
        this.estDeliveryDate = estDeliveryDate;
        this.trackingDetails = trackingDetails;
    }
}
