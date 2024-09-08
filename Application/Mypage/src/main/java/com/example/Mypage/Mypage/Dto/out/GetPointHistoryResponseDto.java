package com.example.Mypage.Mypage.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPointHistoryResponseDto {

    private Long memberId;
    private Integer requestPoint;
    private Integer resultPoint;
    private String type;
    private String date;
}
