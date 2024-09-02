package com.example.Reward.Attendance.Dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendResponseDTO {
    private boolean hasReward;
    private Reward reward;
}
