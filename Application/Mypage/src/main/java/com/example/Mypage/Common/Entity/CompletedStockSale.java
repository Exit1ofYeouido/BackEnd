package com.example.Mypage.Common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompletedStockSale {

    @Id
    @GeneratedValue
    @Column(name = "stock_sale_id")
    private int id;

    private String stockCode;

    private String enterpriseName;

    @Setter
    private double amount;

    private int price;

    private LocalDateTime soldTime;
}
