package com.example.Reward.Attendance.Service;

import com.example.Reward.Attendance.Dto.out.AttendanceRespondDTO;
import com.example.Reward.Attendance.Entity.Attendance;
import com.example.Reward.Attendance.Repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    public AttendanceRespondDTO findAttendInfo(Long memberId) {
        Attendance attendance = attendanceRepository.findByMemberId(memberId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식에 맞게 패턴 설정
        LocalDate recentAttendDate = LocalDate.parse(attendance.getRecent(), formatter);
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        boolean isChecked = recentAttendDate.equals(today);
        int recentMonth = recentAttendDate.getMonthValue();
        int count = (currentMonth==recentMonth) ? attendance.getCount() : 0;

        AttendanceRespondDTO attendanceRespondDTO = AttendanceRespondDTO.builder()
                .isChecked(isChecked)
                .attendCount(count)
                .month(currentMonth)
                .build();

        return attendanceRespondDTO;
    }
}
