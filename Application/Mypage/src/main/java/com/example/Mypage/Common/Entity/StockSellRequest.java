package com.example.Mypage.Common.Entity;

public interface StockSellRequest {
    
    Long getId();

    Member getMember();

    String getEnterpriseName();

    String getStockCode();

    Double getAmount();

    String toString();
}
