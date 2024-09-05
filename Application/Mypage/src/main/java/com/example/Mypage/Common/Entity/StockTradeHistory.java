package com.example.Mypage.Common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTradeHistory {

    @Id
    @GeneratedValue
    @Column(name = "trade_id")
    private Long id;

    private String stockName;

    private String tradeType;

    private double count;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "member_stock_id")
    private MemberStock memberStock;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
