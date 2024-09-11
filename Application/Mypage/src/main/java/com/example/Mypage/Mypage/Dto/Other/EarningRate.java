package com.example.Mypage.Mypage.Dto.Other;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EarningRate {

    private String enterpriseName;

    private String earningRate;

    private String stockCode;


}
