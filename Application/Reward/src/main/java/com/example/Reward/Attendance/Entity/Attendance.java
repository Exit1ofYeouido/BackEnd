package com.example.Reward.Attendance.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long attendanceId;
    private Long memberId;
    @Setter
    private int count;
    @Setter
    private String recent;

    public Attendance(Long memberId, String recent) {
        this.memberId = memberId;
        this.count = 0;
        this.recent = recent;
    }
}
