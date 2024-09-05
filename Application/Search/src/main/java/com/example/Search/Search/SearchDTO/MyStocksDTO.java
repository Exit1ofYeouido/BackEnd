package com.example.Search.Search.SearchDTO;

public class MyStocksDTO {
    private String stockName;
    private int stockPrice;
    private Long stockCount;


    public MyStocksDTO(String stockName, int stockPrice, Long stockCount) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.stockCount = stockCount;
    }
}