package com.logistics.packagetracker.aspect;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.enumeration.PackageStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Aspect
@Configuration
public class LoggingAspect
{
    /** This aspect can be used to implement cross cutting concerns like security, logging, caching, emailing etc. The advice is executed along with the Join Point. */
    
    @AfterReturning(value = "@annotation(Loggable)", returning = "returnValue")
    public void logPackageMethodCall(JoinPoint joinPoint, Object returnValue)
    {
        Package pack = (Package) returnValue;
        logTracker(joinPoint, pack);
        log.info("Logged tracker details via aspect");
    }
    
    @AfterThrowing(value = "@annotation(Loggable)", throwing = "exception")
    public void logPackageMethodException(JoinPoint joinPoint, Throwable exception)
    {
        log.info("Cause: {}, Message: {}", exception.getCause(), exception.getMessage());
        log.info("Logged via aspect after throwing");
    }
    
    private void logTracker(JoinPoint joinPoint, Package pack)
    {
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
    }
}
