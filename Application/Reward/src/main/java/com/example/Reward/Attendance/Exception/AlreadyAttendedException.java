package com.example.Reward.Attendance.Exception;

import com.example.Reward.Receipt.Exception.ReceiptException;
import lombok.Getter;

@Getter
public class AlreadyAttendedException extends AttendanceException {
    public AlreadyAttendedException() {
        super(AttendanceErrorStatus.ALREADY_ATTENDED);
    }
}
