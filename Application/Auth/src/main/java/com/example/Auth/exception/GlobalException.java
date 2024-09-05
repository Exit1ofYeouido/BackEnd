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
    public ResponseEntity<ErrorResult> handleJwtException(JwtException e) {
        log.error("JWT 오류 : {}", e.getMessage());

        ErrorResult errorResult = ErrorResult.builder()
                .error("유효하지 않은 토큰입니다.")
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VerificationCodeRequestException.class)
    public ResponseEntity<ErrorResult> handleVerificationCodeRequestException(VerificationCodeRequestException e) {
        log.error("인증번호 발급 오류 {}", e.getMessage());

        ErrorResult errorResult = ErrorResult.builder()
                .error("인증번호 발급 오류")
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MembernameNotValidException.class)
    public ResponseEntity<ErrorResult> handleUsernameTooShort(MembernameNotValidException e) {
        ErrorResult errorResult = ErrorResult.builder()
                .error("유효하지 않은 형식입니다.")
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorResult);
    }
}
