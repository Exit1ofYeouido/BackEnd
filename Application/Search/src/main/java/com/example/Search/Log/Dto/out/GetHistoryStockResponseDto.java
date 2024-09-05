package com.example.Search.Log.Dto.out;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetHistoryStockResponseDto {

    private String date;

    private Integer totalCount;

    private Integer holdingCount;

    private Integer notHoldingCount;


}

