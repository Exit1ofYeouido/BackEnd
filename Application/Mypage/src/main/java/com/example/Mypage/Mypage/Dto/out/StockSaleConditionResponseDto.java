package com.example.Mypage.Mypage.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockSaleConditionResponseDto {

    private String holdingAmount;

    private String minSaleAmount;

    private boolean isSellable;
}
