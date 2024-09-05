package com.example.Search.Log.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSearchLogMemberStockDto {
    private Boolean isHolding;
    private List<MemberStockCountDto> countResult;
}
