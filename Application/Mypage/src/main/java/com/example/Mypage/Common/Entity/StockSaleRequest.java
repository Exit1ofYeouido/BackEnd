package com.example.Mypage.Common.Entity;

import java.time.LocalDateTime;

public interface StockSaleRequest {

    Long getId();

    Member getMember();

    String getEnterpriseName();

    String getStockCode();

    Double getAmount();

    LocalDateTime getSaleDate();

    String toString();
}
