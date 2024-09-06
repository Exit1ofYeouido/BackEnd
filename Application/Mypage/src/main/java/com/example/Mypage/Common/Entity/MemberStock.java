package com.example.Mypage.Common.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="member_stock")
public class MemberStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_stock_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private double amount;

    private int averagePrice;

    private String stockCode;

    private String stockName;

    private double availableAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
