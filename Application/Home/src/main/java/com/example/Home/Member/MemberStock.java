package com.example.Home.Member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "member_stock")
public class MemberStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_stock_id")
    private Long memberStockId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name="code", columnDefinition = "char(6)")
    private String code;

    @Column(name = "count")
    private Double count;

    @Setter
    @Column(name = "average_price")
    private Long averagePrice;

    public Long getAveragePrice() {
        return averagePrice;
    }

    public Long getMemberStockId() {
        return memberStockId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getCode() {
        return code;
    }

    public Double getCount() {
        return count;
    }
}