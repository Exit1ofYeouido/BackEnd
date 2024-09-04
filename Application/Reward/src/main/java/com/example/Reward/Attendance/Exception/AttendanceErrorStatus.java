package com.example.Reward.Attendance.Exception;

import lombok.Getter;

@Getter
public enum AttendanceErrorStatus {
    ALREADY_ATTENDED(400, "이미 출석했습니다 !!");

    private final int status;
    private final String message;

    AttendanceErrorStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
