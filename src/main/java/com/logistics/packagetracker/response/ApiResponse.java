package com.logistics.packagetracker.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class ApiResponse
{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;
    private Object responseBody;
    
    public ApiResponse(HttpStatus status, String message, List<String> errors, Object responseBody)
    {
        super();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.responseBody = responseBody;
    }
    
    public ApiResponse(HttpStatus status, String message, String error, Object responseBody)
    {
        super();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.responseBody = responseBody;
        errors = Collections.singletonList(error);
    }
}