package com.example.Reward.Attendance.Dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Reward {
    private String enterpriseName;
    private double amount;
    private String stockCode;
}
