package com.example.Search.Common.Entity;

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
public class MemberStock {


    @Id
    @GeneratedValue
    @Column(name = "member_stock_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    private double count;

    private int averagePrice;

    private String stockCode;

    @Column(unique = true)
    private String stockName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}