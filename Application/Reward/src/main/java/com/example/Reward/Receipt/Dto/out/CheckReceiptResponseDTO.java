package com.example.Reward.Receipt.Dto.out;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CheckReceiptResponseDTO {

    @Setter
    private boolean find;

    private String storeName;

    private String price;

    private String dealTime;

    private String approvalNum;

    @Setter
    private String imgURL;

    @Setter
    private String enterpriseName;
}
