package com.example.Home.Common.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "member_stock")
public class MemberStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_stock_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name="stock_code", columnDefinition = "char(6)")
    private String code;

    private Double amount;

    @Setter
    @Column(name = "average_price")
    private Long averagePrice;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Double availableAmount;

}