package com.logistics.packagetracker.entity;

import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackageDTO
{
    private String id;
    private String trackingCode;
    private PackageStatus status;
    private String currentSource;
    private String currentCity;
    private String currentState;
    private String currentCountry;
    private String currentZipcode;
}
