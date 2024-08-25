package com.example.Mypage.Common.Entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Trade {

    @Id
    @GeneratedValue
    private Long id;

    private String stockName;

    private String tradeType;

    private String rewardType;

    private String code;

    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name="memberStock_id")
    private MemberStock memberStock;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;




}
