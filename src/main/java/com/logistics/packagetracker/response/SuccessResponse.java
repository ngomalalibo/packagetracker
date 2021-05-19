package com.logistics.packagetracker.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuccessResponse
{
    
    private String message;
    private Object object;
    private int status;
}