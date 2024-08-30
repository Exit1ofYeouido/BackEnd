package com.example.Reward.Attendance.Controller;

import com.example.Reward.Attendance.Dto.out.AttendanceRespondDTO;
import com.example.Reward.Attendance.Service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
@Tag(name="출석체크 API")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/")
    @Operation(description = "이번 달 출석 현황 조회")
    public ResponseEntity<AttendanceRespondDTO> getAttendInfo(@RequestHeader("memberId") String memberId) {
        AttendanceRespondDTO attendanceRespondDTO = attendanceService.findAttendInfo(Long.valueOf(memberId));
        return ResponseEntity.ok(attendanceRespondDTO);
    }
}
