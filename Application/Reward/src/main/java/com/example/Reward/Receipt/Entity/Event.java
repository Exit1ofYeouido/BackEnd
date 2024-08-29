package com.example.Reward.Receipt.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(unique = true)
    private String enterpriseName;

    @Column(unique = true)
    private String code;

    @Setter
    private double rewardAmount;

}
