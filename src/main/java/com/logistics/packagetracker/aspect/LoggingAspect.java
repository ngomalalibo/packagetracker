package com.logistics.packagetracker.aspect;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.TrackerDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Slf4j
@Aspect
@Configuration
public class LoggingAspect
{
    
    @Autowired
    TrackerDetailsService trackerDetailsService;
    
    public LoggingAspect(TrackerDetailsService trackerDetailsService)
    {
        this.trackerDetailsService = trackerDetailsService;
    }
    
    @AfterReturning(value = "@annotation(Loggable)", returning = "returnValue")
    public void logPackageMethodCall(JoinPoint joinPoint, Object returnValue)
    {
        PackageStatus status = (PackageStatus) returnValue;
        logTracker(joinPoint, status);
        log.info("Logged tracker details via aspect");
    }
    
    @AfterThrowing(value = "@annotation(Loggable)", throwing = "exception")
    public void logPackageMethodException(JoinPoint joinPoint, Throwable exception)
    {
        log.info("Cause: {}, Message: {}", exception.getCause(), exception.getMessage());
        log.info("Logged via aspect after throwing");
    }
    
    private void logTracker(JoinPoint joinPoint, PackageStatus status)
    {
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        TrackingDetails details = new TrackingDetails(null, Package.generateTrackingCode(), status, ZonedDateTime.now().toString(), "Fedex",
                                                     "Ikeja", "Lagos", "Nigeria", "100001");
        trackerDetailsService.save(details);
    }
}
