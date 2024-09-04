package com.example.Search.SearchDTO;

import lombok.Getter;

import java.util.List;

@Getter
public class StockPriceListDTO {
    private List<DailyStockPriceDTO> stockPriceList;

    public StockPriceListDTO(List<DailyStockPriceDTO> stockPriceList) {
        this.stockPriceList = stockPriceList;
    }
}