package com.example.Reward.Advertisement.Entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="check_today")
public class CheckToday {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String enterpriseName;

    private Long memberId;




}
