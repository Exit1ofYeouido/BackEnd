package com.example.Reward.Advertisement.Exception;

import com.example.Reward.Advertisement.Controller.AdController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackageClasses = AdController.class)
public class AdExceptionHandler {

//    @ExceptionHandler(DuplicationMemberEmailException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    private ResponseEntity<MemberResponse> handler(DuplicationMemberEmailException e) {
//
//        MemberResponse response = MemberResponse.builder()
//                .status(e.getStatus())
//                .message(e.getMessage())
//                .data(e.getEmail())
//                .build();
//
//        return ResponseEntity.badRequest().body(response);
//    }

    @ExceptionHandler(NoStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<AdResponse> handler(NoStockException e) {

        AdResponse response = AdResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotMatchedEnterpriseName.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<AdResponse> handler(NotMatchedEnterpriseName e) {

        AdResponse response = AdResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .data(e.getCurrentName())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotAccessTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ResponseEntity<AdResponse> handler(NotAccessTokenException e) {

        AdResponse response = AdResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotFoundMediaLinkException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<AdResponse> handler(NotFoundMediaLinkException e) {

        AdResponse response = AdResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }


}
