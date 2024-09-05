package com.example.Search.Search.SearchDTO;

import com.example.Search.Search.SearchDTO.DailyStockPriceDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class StockPriceListDTO {
    private List<DailyStockPriceDTO> stockPriceList;

    public StockPriceListDTO(List<DailyStockPriceDTO> stockPriceList) {
        this.stockPriceList = stockPriceList;
    }
}