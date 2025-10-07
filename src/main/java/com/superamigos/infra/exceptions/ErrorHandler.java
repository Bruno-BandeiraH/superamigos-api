package com.superamigos.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity businessErrorHandler(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
