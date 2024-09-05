package com.example.Mypage.Common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock_sale_request_b")
public class StockSaleRequestB implements StockSaleRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_sell_request_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String enterpriseName;

    private String stockCode;

    private Double amount;

    @Override
    public String toString() {
        return "StockSellRequest{" +
                "id=" + id +
                ", member=" + (member != null ? member.getId() : "null") +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", amount=" + amount +
                '}';
    }
}
