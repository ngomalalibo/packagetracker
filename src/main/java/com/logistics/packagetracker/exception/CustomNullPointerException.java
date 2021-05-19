package com.logistics.packagetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //map exception to HTTP status so it can be caught with a view
public class CustomNullPointerException extends NullPointerException
{
    public CustomNullPointerException()
    {
        super();
    }
    
    public CustomNullPointerException(String s)
    {
        super(s);
    }
}
