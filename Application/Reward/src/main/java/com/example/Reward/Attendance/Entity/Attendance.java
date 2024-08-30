package com.example.Reward.Attendance.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Attendance {
    @Id
    private Long attendanceId;
    private Long memberId;
    private int count;
    private String recent;
}
