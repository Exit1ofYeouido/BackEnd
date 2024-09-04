package com.example.Reward.Receipt.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String enterpriseName;
    private String code;
    private double rewardAmount;
}
