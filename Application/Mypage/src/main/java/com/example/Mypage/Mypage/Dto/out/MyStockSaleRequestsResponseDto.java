package com.example.Mypage.Mypage.Dto.out;

import java.util.List;
import lombok.Data;

@Data
public class MyStockSaleRequestsResponseDto {
    private int size;

    private List<MyStockSaleRequestResponseDto> myStockSaleRequestResponseDtos;

    public MyStockSaleRequestsResponseDto(List<MyStockSaleRequestResponseDto> myStockSaleRequestResponseDtos) {
        this.myStockSaleRequestResponseDtos = myStockSaleRequestResponseDtos;
        this.size = myStockSaleRequestResponseDtos.size();
    }
}
