package com.example.Reward.Attendance.Controller;

import com.example.Reward.Attendance.Dto.out.AttendResponseDTO;
import com.example.Reward.Attendance.Dto.out.GetAttendanceResponseDTO;
import com.example.Reward.Attendance.Dto.out.StockInfoDTO;
import com.example.Reward.Attendance.Service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<GetAttendanceResponseDTO> getAttendInfo(@RequestHeader("memberId") String memberId) {
        GetAttendanceResponseDTO getAttendanceResponseDTO = attendanceService.findAttendInfo(Long.valueOf(memberId));
        return ResponseEntity.ok(getAttendanceResponseDTO);
    }

    @PostMapping("/check")
    @Operation(description = "출석 체크 요청")
    public ResponseEntity<AttendResponseDTO> postAttend(@RequestHeader("memberId") String memberId) {
        Boolean hasReward = attendanceService.attend(Long.valueOf(memberId));
        if(!hasReward) {
            AttendResponseDTO attendResponseDTO = AttendResponseDTO.builder().hasReward(false).build();
            return ResponseEntity.ok(attendResponseDTO);
        }
        StockInfoDTO stockInfoDTO = attendanceService.getRandomStock(Long.valueOf(memberId));
        AttendResponseDTO attendResponseDTO = AttendResponseDTO.builder().hasReward(true).build();
        return ResponseEntity.ok(attendResponseDTO);
    }
}
