package com.example.Mypage.Mypage.Dto.out;


import com.example.Mypage.Mypage.Dto.Other.EarningRate;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetAllMyPageResponseDto {

    private String earningRate;

    private int allCost;

    private String accountName;

    private String accountId;

    private Long totalPoint;

    private List<EarningRate> earningRates;



    public static GetAllMyPageResponseDto of(Long totalPoint, String calcAssetsEarningRate, List<EarningRate> earningRates, int allCost){
        GetAccountResponseDto getAccountResponseDto=new GetAccountResponseDto();

        return GetAllMyPageResponseDto.builder()
                .allCost(allCost)
                .earningRate(calcAssetsEarningRate)
                .accountName(getAccountResponseDto.getAccountName())
                .accountId(getAccountResponseDto.getAccountId())
                .earningRates(earningRates)
                .totalPoint(totalPoint).build();

    }

}
