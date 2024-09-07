package com.example.Mypage.Common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "stock_sale_request_a")
public class StockSaleRequestA implements StockSaleRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_sale_request_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String enterpriseName;

    private String stockCode;

    private Double amount;

    @Column(name = "created_at")
    private LocalDateTime saleDate;

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
