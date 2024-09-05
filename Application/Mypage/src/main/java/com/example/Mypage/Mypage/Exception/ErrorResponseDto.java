package com.example.Mypage.Mypage.Exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ErrorResponseDto {

    private String message;

    private LocalDateTime time;
}
