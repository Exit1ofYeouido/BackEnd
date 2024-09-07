package com.example.Mypage.Mypage.Dto.Other;

import com.example.Mypage.Mypage.Dto.out.GetPointHistoryResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointHistoryResponsesDto {

    private int size;

    private List<GetPointHistoryResponseDto> pointHistory;
}
