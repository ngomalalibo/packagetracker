package com.logistics.packagetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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
