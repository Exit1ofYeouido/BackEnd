package com.example.Mypage.Mypage.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyStocksResponseDto {

    private String name;
    private Double holdStockCount;
    private String earningRate;
}
