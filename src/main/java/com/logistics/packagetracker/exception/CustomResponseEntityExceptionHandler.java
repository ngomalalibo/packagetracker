package com.logistics.packagetracker.exception;

import com.logistics.packagetracker.response.ApiResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors())
        {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors())
        {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors, null);
        
        return buildResponseEntity(apiResponse);
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex)
    {
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage(), null);
        return buildResponseEntity(apiResponse);
    }
    
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerExceptionInternal(
            NullPointerException ex)
    {
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage(), null);
        return buildResponseEntity(apiResponse);
    }
    
    @ExceptionHandler(CustomNullPointerException.class)
    protected ResponseEntity<Object> handleCustomNullPointerExceptionInternal(
            CustomNullPointerException ex)
    {
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage(), null);
        return buildResponseEntity(apiResponse);
    }
    
    @ExceptionHandler(NonUniqueResultException.class)
    protected ResponseEntity<Object> handleNonUniqueResultExceptionInternal(
            NonUniqueResultException ex)
    {
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.NOT_ACCEPTABLE, ex.getLocalizedMessage(), ex.getMessage(), null);
        return buildResponseEntity(apiResponse);
    }
    
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request)
    {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations())
        {
            errors.add(violation.getRootBeanClass().getName() + " " +
                               violation.getPropertyPath() + ": " + violation.getMessage());
        }
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors, null);
        return buildResponseEntity(apiResponse);
    }
    
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request)
    {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error, null);
        return buildResponseEntity(apiResponse);
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        
        ApiResponse apiResponse = new ApiResponse(HttpStatus.METHOD_NOT_ALLOWED,
                                                  ex.getLocalizedMessage(), builder.toString(), null);
        return buildResponseEntity(apiResponse);
    }
    
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        String template = "Missing parameter:  %s. Missing parameter: %s";
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), String.format(template, ex.getMessage(), ex.getParameter()), null);
        return handleExceptionInternal(
                ex, apiResponse, headers, apiResponse.getStatus(), request);
    }
    
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request)
    {
        String error = ex.getParameterName() + " parameter is missing";
        
        ApiResponse apiResponse =
                new ApiResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error, null);
        return buildResponseEntity(apiResponse);
    }
    
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request)
    {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred", null);
        return buildResponseEntity(apiResponse);
    }
    
    private ResponseEntity<Object> buildResponseEntity(ApiResponse apiResponse)
    {
        return new ResponseEntity<Object>(apiResponse, new HttpHeaders(), apiResponse.getStatus());
    }
}
