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
    @Column(name="check_today_id")
    private Long id;
    
    private String enterpriseName;

    private Long memberId;




}
