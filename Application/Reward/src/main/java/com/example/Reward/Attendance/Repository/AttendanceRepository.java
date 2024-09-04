package com.example.Reward.Attendance.Repository;

import com.example.Reward.Attendance.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Attendance findByMemberId(Long memberId);
}
