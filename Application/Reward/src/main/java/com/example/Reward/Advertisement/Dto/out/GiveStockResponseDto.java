package com.example.Reward.Advertisement.Dto.out;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiveStockResponseDto {
    private String name;

    private double amount;

    public static GiveStockResponseDto givetostock(String enterprisename, double amount) {

        return GiveStockResponseDto.builder().name(enterprisename).amount(amount).build();


    }
}
