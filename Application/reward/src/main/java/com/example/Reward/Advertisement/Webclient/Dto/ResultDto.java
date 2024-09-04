package com.example.Reward.Advertisement.Webclient.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResultDto {

    private int cost;

    private double amount;
}
