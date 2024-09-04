package com.example.Reward.Attendance.Exception;

import com.example.Reward.Common.Response.CommonResponse;
import lombok.Builder;

public class AttendanceErrorResponse extends CommonResponse {
    @Builder
    public AttendanceErrorResponse(int status, String message) {
        super(status, message);
    }

    public static AttendanceErrorResponse error(String message) {
        return new AttendanceErrorResponse(400, message);
    }
}
