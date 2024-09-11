package com.example.Mypage.Mypage.Dto.out;


import com.example.Mypage.Mypage.Dto.Other.EarningRate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAllMyPageResponseDto {

    private String earningRate;

    private int allCost;


    private String accountId;

    private Integer totalPoint;

    private List<EarningRate> earningRates;


    public static GetAllMyPageResponseDto of(Integer totalPoint, String calcAssetsEarningRate,
                                             List<EarningRate> earningRates,String accountId,int allCost) {
        GetAccountResponseDto getAccountResponseDto = new GetAccountResponseDto();

        return GetAllMyPageResponseDto.builder()
                .allCost(allCost)
                .earningRate(calcAssetsEarningRate)
                .accountId(accountId)
                .earningRates(earningRates)
                .totalPoint(totalPoint).build();

    }

}
