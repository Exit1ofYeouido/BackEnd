package com.example.Search.Search.SearchDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DailyStockPriceDTO {

    private LocalDate date;
    private long price;

    public DailyStockPriceDTO(LocalDate date, long price) {
        this.date = date;
        this.price = price;
    }
}