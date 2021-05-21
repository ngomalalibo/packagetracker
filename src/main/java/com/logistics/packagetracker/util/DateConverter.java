package com.logistics.packagetracker.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateConverter
{
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy HH:mm:ss a Z");
    
    public static String zonedDateTimeToString(ZonedDateTime zdt)
    {
        return zdt.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy HH:mm:ss a Z"));
    }
    
    public static ZonedDateTime stringToDateConverter(String date)
    {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        localDateTime.atZone(TimeZone.getDefault().toZoneId());
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
        return offsetDateTime.toZonedDateTime();
        // return offsetDateTime.atZoneSimilarLocal(TimeZone.getDefault().toZoneId());
        // return offsetDateTime.atZoneSameInstant(ZoneId.systemDefault());
    }
}