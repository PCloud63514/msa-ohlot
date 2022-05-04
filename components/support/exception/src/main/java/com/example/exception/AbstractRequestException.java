package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AbstractRequestException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;

    public AbstractRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.printStackTrace();
    }
}
