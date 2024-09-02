package com.example.Search.SearchDTO;

import lombok.Getter;

import java.util.List;

@Getter
public class StockDetailDTO {
    private String stockName;
    private String stockCode;
    private int stockPrice;
    private double stockCount;
    private String previousPrice;
    private String previousRate;
    private List<DailyStockPriceDTO> stockPriceList;


    public StockDetailDTO(String stockName, String stockCode, int stockPrice, double stockCount, String previousPrice, String previousRate, List<DailyStockPriceDTO> stockPriceList) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.stockPrice = stockPrice;
        this.stockCount = stockCount;
        this.previousPrice = previousPrice;
        this.previousRate = previousRate;
        this.stockPriceList = stockPriceList;
    }
}