package com.example.Reward.Receipt.Dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalyzeReceiptDTO {
    private String storeName;
    private String price;
    private String dealTime;
    private String approvalNum;
}
