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


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGrobalException(Exception e){
//        ErrorResponseDto errorResponseDto=new ErrorResponseDto(
//                e.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(NotMemberException.class)
    public ResponseEntity<String> notmemberException(NotMemberException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("멤버가 존재하지않습니다."+e.getMemId());
    }
}
