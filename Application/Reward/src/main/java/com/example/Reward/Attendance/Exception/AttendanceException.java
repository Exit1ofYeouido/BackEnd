package com.example.Reward.Attendance.Exception;

public class AttendanceException extends RuntimeException {
    private final AttendanceErrorStatus attendanceErrorStatus;

    public AttendanceException(AttendanceErrorStatus attendanceErrorStatus) {
        this.attendanceErrorStatus = attendanceErrorStatus;
    }

    public int getStatus() {
        return attendanceErrorStatus.getStatus();
    }

    public String getMessage() {
        return attendanceErrorStatus.getMessage();
    }
}
