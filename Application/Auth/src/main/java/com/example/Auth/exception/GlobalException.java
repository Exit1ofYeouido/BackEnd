package com.example.Auth.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtException(JwtException e) {
        log.error("JWT 오류 : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰이 유효하지 않습니다.");
    }
}
