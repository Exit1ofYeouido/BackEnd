package com.example.Mypage.Common.Entity;

public interface StockSaleRequest {
    
    Long getId();

    Member getMember();

    String getEnterpriseName();

    String getStockCode();

    Double getAmount();

    String toString();
}
