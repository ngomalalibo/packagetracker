package com.logistics.packagetracker.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateToLocalDateTimeConverter
{
    static SimpleDateFormat normal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat custom = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss a Z");
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy HH:mm:ss a Z");
    
    public static ZonedDateTime stringToDateConverter(String date)
    {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        localDateTime.atZone(TimeZone.getDefault().toZoneId());
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
        return offsetDateTime.toZonedDateTime();
    }
    
    public static String zonedDateTimeToString(ZonedDateTime zdt)
    {
        return zdt.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy HH:mm:ss a Z"));
    }
    
    public static String localDateTimeToString(LocalDateTime localDateTime)
    {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant()).toString();
    }
    
    public static LocalDateTime stringToLocalDateTime(String date)
    {
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        localDateTime.atZone(TimeZone.getDefault().toZoneId());
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
        return offsetDateTime.toZonedDateTime().toLocalDateTime();
    }
}