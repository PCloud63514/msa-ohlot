package com.dreamsecurity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestExceptionResponse {
    private HttpStatus httpStatus;
    private String message;
}
