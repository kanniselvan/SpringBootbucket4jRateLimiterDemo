package com.kanni.ConcurrentAccessDemo.exception;

import com.kanni.ConcurrentAccessDemo.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = { Exception.class })
    public ErrorResponse unKnownException(Exception ex)
    {
        return new ErrorResponse(400, ex.getMessage());
    }
}
