package com.example.Mypage.Common.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberStock {


    @Id
    @GeneratedValue
    @Column(name="member_stock_id")
    private Long id;

    private double count;


    private int averagePrice;


    private String stockCode;

    private String stockName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;




}
