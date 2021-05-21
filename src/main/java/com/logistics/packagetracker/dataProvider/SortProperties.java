package com.logistics.packagetracker.dataProvider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortProperties
{
    /** Sort object used along with mongo document properties to sort database queries. */
    private String propertyName;
    private boolean ascending;
    
    public SortProperties(String propertyName, boolean ascending)
    {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }
    
    public SortProperties()
    {
        super();
    }
    
}