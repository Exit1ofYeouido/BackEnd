package com.example.Mypage.Mypage.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTutorialCheckResponseDto {

    private boolean tutoChecked;

    public static GetTutorialCheckResponseDto of(boolean b) {
        return GetTutorialCheckResponseDto
                .builder()
                .tutoChecked(b)
                .build();
    }
}
