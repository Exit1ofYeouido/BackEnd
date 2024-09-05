package com.example.Search.Log.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberStockCountByYearDto {
    private int month;
    private Long totalCount;
}
