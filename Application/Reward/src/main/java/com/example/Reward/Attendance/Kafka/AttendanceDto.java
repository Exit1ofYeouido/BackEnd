package com.example.Reward.Attendance.Kafka;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDto {
    private Long memberId;
}
