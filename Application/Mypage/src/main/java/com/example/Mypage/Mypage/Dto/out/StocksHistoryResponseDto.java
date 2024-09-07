package com.example.Mypage.Mypage.Dto.out;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StocksHistoryResponseDto {

    private int size;
    
    private List<MyStocksHistoryResponseDto> stocksHistory;
}
