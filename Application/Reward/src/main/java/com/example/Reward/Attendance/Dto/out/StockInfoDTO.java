package com.example.Reward.Attendance.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockInfoDTO {
    private Long eventId;
    private Long contentId;
    private String enterpriseName;
    private String code;
}
