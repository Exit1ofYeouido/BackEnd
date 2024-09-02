package com.example.Mypage.Mypage.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private String message;

    private LocalDateTime time;
}
