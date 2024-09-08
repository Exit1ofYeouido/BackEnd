package com.example.Mypage.Mypage.Exception;

import jakarta.persistence.PessimisticLockException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<String> handleDuplicateIdException(DuplicateIdException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PessimisticLockException.class)
    public ResponseEntity<String> handlePessimisticLockException(PessimisticLockException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Lock 오류");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountNotFoundException(AccountNotFoundException e) {
        log.error(e.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ExtractTrIdException.class)
    public ResponseEntity<ErrorResponseDto> handleExtractTrIdException(ExtractTrIdException e) {
        log.error(e.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponseDto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseDto> handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException e) {
        log.error(e.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(InValidStockCodeException.class)
    public ResponseEntity<ErrorResponseDto> handleInValidStockCodeException(InValidStockCodeException e) {
        log.error(e.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(ExceedSaleAmountException.class)
    public ResponseEntity<ErrorResponseDto> handleExceedSaleAmountException(ExceedSaleAmountException e) {
        log.error(e.getMessage());
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}
