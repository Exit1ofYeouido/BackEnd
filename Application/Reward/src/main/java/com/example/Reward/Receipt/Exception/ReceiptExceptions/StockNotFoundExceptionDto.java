package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import lombok.Getter;

@Getter
public class StockNotFoundExceptionDto {
    private String foundName;
    private String receiptURL;

    public StockNotFoundExceptionDto(String foundName, String receiptURL) {
        this.foundName = foundName;
        this.receiptURL = receiptURL;
    }
}
