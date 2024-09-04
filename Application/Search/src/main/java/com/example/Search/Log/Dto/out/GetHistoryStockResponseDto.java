package com.example.Search.Log.Dto.out;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetHistoryStockResponseDto {

    private LocalDateTime date;

    private Integer totalCount;

    private Integer holdingCount;

    private Integer notHoldingCount;


}

