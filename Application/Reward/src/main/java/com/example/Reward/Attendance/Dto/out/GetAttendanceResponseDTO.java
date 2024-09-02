package com.example.Reward.Attendance.Dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAttendanceResponseDTO {
    private boolean isChecked;
    private int month;
    private int attendCount;
}
