package com.saarth.app.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class Error {
    private HttpStatus statusCode;
    private String message;
    private String error;
    private LocalDateTime timestamp;
}
