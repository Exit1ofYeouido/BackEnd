package com.example.Mypage.Mypage.Dto.in;

import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Entity.StockSaleRequestA;
import com.example.Mypage.Common.Entity.StockSaleRequestB;
import lombok.Data;

@Data
public class StockSellRequestDto {

    private String stockCode;

    private String stockName;

    private Double sellAmount;

    public StockSaleRequestA toSellRequestA(Member member) {
        return StockSaleRequestA.builder()
                .amount(sellAmount)
                .enterpriseName(stockName)
                .stockCode(stockCode)
                .member(member)
                .build();
    }

    public StockSaleRequestB toSellRequestB(Member member) {
        return StockSaleRequestB.builder()
                .amount(sellAmount)
                .enterpriseName(stockName)
                .stockCode(stockCode)
                .member(member)
                .build();
    }
}
