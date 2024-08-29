package com.example.Reward.Receipt.Dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RewardResponseDTO {

    private String name;

    private double amount;
}
