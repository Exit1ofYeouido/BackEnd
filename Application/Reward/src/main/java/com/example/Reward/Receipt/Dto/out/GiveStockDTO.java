package com.example.Reward.Receipt.Dto.out;

import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.clients.consumer.StickyAssignor;

@Getter
@Builder
public class GiveStockDTO {
    private Long memberId;
    private String enterpriseName;
    private String code;
    private Integer price;
    private double amount;
}
