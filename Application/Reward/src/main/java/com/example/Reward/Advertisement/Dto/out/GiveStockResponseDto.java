package com.example.Reward.Advertisement.Dto.out;


import com.example.Reward.Advertisement.Dto.in.GiveStockRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiveStockResponseDto {
    private String name;

    private double amount;

    public static GiveStockResponseDto givetostock(String enterprisename) {

        return GiveStockResponseDto.builder().name(enterprisename).amount(0.2).build();


    }
}
