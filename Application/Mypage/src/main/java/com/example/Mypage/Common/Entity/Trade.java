package com.example.Mypage.Common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class Trade {

    @Id
    @GeneratedValue
    @Column(name="trade_id")
    private Long id;

    private String stockName;

    private String tradeType;

    private String rewardType;

    @Column(length = 6)
    private String code;

    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "memberStock_id")
    private MemberStock memberStock;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
