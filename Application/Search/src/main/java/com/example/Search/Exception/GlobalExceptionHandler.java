package com.example.Search.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity<ErrorResult> handleNotAdminException(NotAdminException e) {
        ErrorResult errorResult = ErrorResult.builder().message(e.getMessage())
                .error("NOT ADMIN").build();

        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }
}
