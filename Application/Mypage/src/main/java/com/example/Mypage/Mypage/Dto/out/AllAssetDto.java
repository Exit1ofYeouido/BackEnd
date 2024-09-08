package com.example.Mypage.Mypage.Dto.out;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllAssetDto {

    private String calcAssetsEarningRate;

    private Integer allCost;
}
