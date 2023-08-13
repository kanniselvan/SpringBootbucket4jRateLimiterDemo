package com.kanni.ConcurrentAccessDemo.exception;

import com.google.gson.Gson;
import com.kanni.ConcurrentAccessDemo.model.ErrorRes;
import com.kanni.ConcurrentAccessDemo.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    Gson gson = new Gson();

    @ExceptionHandler(value = { RuntimeException.class })
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorRes tooLimit(Exception ex)
    {
        String message=ex.getMessage().split("TooManyRequests: 429 :")[1];
        String res=message.substring(2,message.length()-1);
        System.out.println("ss"+res);
      //  return new ErrorResponse(HttpStatus.TOO_MANY_REQUESTS.value(), ex.getMessage());
        return gson.fromJson(res, ErrorRes.class);
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unKnownException(Exception ex)
    {
        System.out.println("UU"+ex.getMessage());
        return new ErrorResponse(400, ex.getMessage());
    }
}
