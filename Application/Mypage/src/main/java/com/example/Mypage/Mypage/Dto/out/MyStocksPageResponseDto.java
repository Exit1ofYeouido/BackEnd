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
public class MyStocksPageResponseDto {

    private List<MyStocksResponseDto> myStocksResponse;

    private StocksValueResponseDto stocksValueResponseDto;

}
