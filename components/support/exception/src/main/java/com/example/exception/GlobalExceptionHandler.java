package com.example.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AbstractRequestException.class)
    public RequestExceptionResponse handleRequestException(AbstractRequestException e) {
        return new RequestExceptionResponse(e.getHttpStatus(), e.getMessage());
    }
}
