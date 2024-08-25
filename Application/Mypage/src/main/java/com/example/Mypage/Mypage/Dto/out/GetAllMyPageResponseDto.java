package com.example.Mypage.Mypage.Dto.out;


import com.example.Mypage.Mypage.Dto.Other.EarningRate;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetAllMyPageResponseDto {

    private double earningRate;

    private String accountName;

    private String accountId;

    private Long totalPoint;

    private List<EarningRate> earningRates;


    public static GetAllMyPageResponseDto of(Long totalPoint, double calcAssetsEarningRate, List<EarningRate> earningRates){
        GetAccountResponseDto getAccountResponseDto=new GetAccountResponseDto();

        return GetAllMyPageResponseDto.builder()
                .earningRate(calcAssetsEarningRate)
                .accountName(getAccountResponseDto.getAccountName())
                .accountId(getAccountResponseDto.getAccountId())
                .earningRates(earningRates)
                .totalPoint(totalPoint).build();

    }

}
