package com.logistics.packagetracker.enumeration;

public enum PackageStatus
{
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
    
    public String getValue()
    {
        return this.value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    PackageStatus(String value)
    {
        this.value = value;
    }
    
    public String displayText()
    {
        return getDisplayText(this);
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
