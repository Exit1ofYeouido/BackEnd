package com.example.Reward.Attendance.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Attendance {
    @Id
    private Long attendanceId;
    private Long memberId;
    @Setter
    private int count;
    @Setter
    private String recent;
}
