package com.example.Reward.Receipt.Dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiveStockProduceDTO {
    private Long memberId;
    private String enterpriseName;
    private String code;
    private Integer price;
    private double amount;
}
