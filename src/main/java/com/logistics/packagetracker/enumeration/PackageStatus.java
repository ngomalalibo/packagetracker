package com.logistics.packagetracker.enumeration;

import lombok.Getter;

@Getter
public enum PackageStatus
{
    /** Package Tracking statuses are captured using this enumeration */
    PICKED_UP("PICKED_UP"), IN_TRANSIT("IN_TRANSIT"), WAREHOUSE("WAREHOUSE"), DELIVERED("DELIVERED");
    
    public static String getDisplayText(PackageStatus i)
    {
        switch (i)
        {
            case PICKED_UP:
                return "Picked Up";
            case IN_TRANSIT:
                return "In Transit";
            case WAREHOUSE:
                return "Warehouse";
            case DELIVERED:
                return "Delivered";
            default:
                return "";
        }
    }
    
    private String value;
    
    PackageStatus(String value)
    {
        this.value = value;
    }
    
    public static PackageStatus fromValue(String v)
    {
        for (PackageStatus c : PackageStatus.values())
        {
            if (c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
