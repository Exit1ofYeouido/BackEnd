package com.example.Reward.Common.Entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String stockCode;

    private String enterpriseName;

    private LocalDateTime create_at;

    private LocalDateTime update_at;

    private Double rewardAmount;

    private String comment;


    @ManyToOne
    @JoinColumn(name="content_id")
    private Content content;


    public void setRewardAmount(Double rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}
