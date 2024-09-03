package com.example.Mypage.Mypage.Dto.in;

import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Entity.StockSellRequestA;
import com.example.Mypage.Common.Entity.StockSellRequestB;
import lombok.Data;

@Data
public class StockSellRequestDto {

    private String stockCode;

    private String stockName;

    private Double sellAmount;

    public StockSellRequestA toSellRequestA(Member member) {
        return StockSellRequestA.builder()
                .amount(sellAmount)
                .enterpriseName(stockName)
                .stockCode(stockCode)
                .member(member)
                .build();
    }

    public StockSellRequestB toSellRequestB(Member member) {
        return StockSellRequestB.builder()
                .amount(sellAmount)
                .enterpriseName(stockName)
                .stockCode(stockCode)
                .member(member)
                .build();
    }
}
