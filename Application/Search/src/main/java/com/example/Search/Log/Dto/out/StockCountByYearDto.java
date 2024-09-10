package com.example.Search.Log.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCountByYearDto {
    private Integer month;
    private Long totalCount;
    private Long holdingCount;
    private Long notHoldingCount;
}
