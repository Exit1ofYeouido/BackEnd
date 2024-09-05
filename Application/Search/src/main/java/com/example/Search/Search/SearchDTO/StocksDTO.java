package com.example.Search.Search.SearchDTO;

import lombok.Getter;

@Getter
public class StocksDTO {
    private String stockName;
    private String stockCode;
    private int stockPrice;
    private String previousPrice;
    private String previousRate;

    public StocksDTO(String stockName, String stockCode, int stockPrice, String previousPrice, String previousRate) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.stockPrice = stockPrice;
        this.previousPrice = previousPrice;
        this.previousRate = previousRate;
    }
}