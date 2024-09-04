package com.example.Search.SearchDTO;

import lombok.Getter;

import java.util.List;

@Getter
public class StockDetailDTO {
    private String stockName;
    private String stockCode;
    private int stockPrice;
    private double availableAmount;
    private String previousPrice;
    private String previousRate;

    public StockDetailDTO(String stockName, String stockCode, int stockPrice, double availableAmount, String previousPrice, String previousRate) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.stockPrice = stockPrice;
        this.availableAmount = availableAmount;
        this.previousPrice = previousPrice;
        this.previousRate = previousRate;
    }
}