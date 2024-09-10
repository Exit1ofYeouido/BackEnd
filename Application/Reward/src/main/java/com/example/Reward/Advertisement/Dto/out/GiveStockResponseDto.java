package com.example.Reward.Advertisement.Dto.out;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiveStockResponseDto {
    private String name;
    private double amount;
    private String stockCode;

    public static GiveStockResponseDto givetostock(String enterprisename, double amount, String stockCode) {
        return GiveStockResponseDto.builder().name(enterprisename).amount(amount).stockCode(stockCode).build();

    }
}
