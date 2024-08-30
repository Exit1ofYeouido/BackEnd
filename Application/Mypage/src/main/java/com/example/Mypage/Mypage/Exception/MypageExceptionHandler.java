package com.example.Mypage.Mypage.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class MypageExceptionHandler {


//    @ExceptionHandler
//    public


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGrobalException(Exception e){
        ErrorResponseDto errorResponseDto=new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handelGrobalException(Exception e){
        ErrorResponseDto errorResponseDto=new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto,HttpStatus.BAD_REQUEST);
    }
}
