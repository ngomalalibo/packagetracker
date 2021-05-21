package com.logistics.packagetracker.util;

import java.util.UUID;

public class GenerateTrackingCode
{
    public static String generateTrackingCode()
    {
        
        String raw = UUID.randomUUID().toString();
        String[] sect = raw.split("-");
        StringBuilder dd = new StringBuilder();
        for (int i = 0; i < sect.length; i++)
        {
            dd.append(sect[i].charAt(2));
        }
        return dd.toString().toUpperCase();
    }
}
