package com.example.Reward.Receipt.Dto.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RewardRequestDTO {

    private String enterpriseName;

    private String storeName;

    private String price;

    private String dealTime;

    private String approvalNum;

    private String imgURL;
}
