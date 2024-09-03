package com.example.Reward.Attendance.Exception;

import com.example.Reward.Attendance.Controller.AttendanceController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = AttendanceController.class)
public class AttendanceExceptionHandler {

    @ExceptionHandler(AlreadyAttendedException.class)
    private ResponseEntity<AttendanceErrorResponse> handler(AlreadyAttendedException e) {
        AttendanceErrorResponse response = AttendanceErrorResponse.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
