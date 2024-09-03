package com.example.Reward.Attendance.Kafka;

import com.example.Reward.Attendance.Entity.Attendance;
import com.example.Reward.Attendance.Repository.AttendanceRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class KafkaService {
    AttendanceRepository attendanceRepository;

    public KafkaService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @KafkaListener(topics="attendance", groupId = "group_1")
    public void listener(AttendanceDto attendanceDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Long memberId = attendanceDto.getMemberId();
        String yesterday = LocalDate.now().minusDays(1).format(formatter);
        Attendance attendance = new Attendance(memberId, yesterday);
        attendanceRepository.save(attendance);
    }
}
