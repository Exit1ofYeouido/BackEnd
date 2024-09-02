package com.example.Search.SearchRepository.MemberStockRepository;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "member_stock", schema = "mo")
public class MemberStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_stock_id")
    private Long memberStockId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "count")
    private Double count;


    @Column(name = "code", columnDefinition = "char(6)")
    private String stockCode;

    public String getStockCode() {
        return stockCode;
    }
}