package com.example.Search.Log.Dto.out;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCountDto {
    private Date date;
    private Long totalCount;
    private Long holdingCount;
    private Long notHoldingCount;
}

