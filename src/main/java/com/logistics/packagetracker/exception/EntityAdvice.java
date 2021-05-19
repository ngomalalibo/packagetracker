package com.logistics.packagetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class EntityAdvice
{
    
    @ResponseBody
    @ExceptionHandler(EntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String entityExceptionHandler(EntityException ex)
    {
        return ex.getMessage();
    }
}