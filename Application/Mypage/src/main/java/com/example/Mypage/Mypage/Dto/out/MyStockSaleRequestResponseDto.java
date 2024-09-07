package com.example.Mypage.Mypage.Dto.out;

import com.example.Mypage.Common.Entity.StockSaleRequest;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class MyStockSaleRequestResponseDto {

    private Long saleId;
    private Long memberId;
    private String enterpriseName;
    private String stockCode;
    private double amount;
    private String date;

    public MyStockSaleRequestResponseDto(StockSaleRequest stockSaleRequest) {
        this.saleId = stockSaleRequest.getId();
        this.memberId = stockSaleRequest.getMember().getId();
        this.enterpriseName = stockSaleRequest.getEnterpriseName();
        this.stockCode = stockSaleRequest.getStockCode();
        this.amount = stockSaleRequest.getAmount();
        this.date = stockSaleRequest.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
    }
}
