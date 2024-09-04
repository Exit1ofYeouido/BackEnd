package com.example.Search.Search.SearchDTO;

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
    private List<DailyStockPriceDTO> stockPriceList;


    public StockDetailDTO(String stockName, String stockCode, int stockPrice, double availableAmount, String previousPrice, String previousRate, List<DailyStockPriceDTO> stockPriceList) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.stockPrice = stockPrice;
        this.availableAmount = availableAmount;
        this.previousPrice = previousPrice;
        this.previousRate = previousRate;
        this.stockPriceList = stockPriceList;
    }
}