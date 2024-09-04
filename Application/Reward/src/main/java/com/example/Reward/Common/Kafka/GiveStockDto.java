package com.example.Reward.Common.Kafka;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonSerialize
@JsonDeserialize
public class GiveStockDto {

    private Long memId;

    private String enterpriseName;

    private int price;

    private double amount;

    private String code;

}
