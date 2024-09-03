package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import lombok.Getter;

@Getter
public class MissingOcrInfoExceptionDto {
    private String url;
    private String missingValue;

    public MissingOcrInfoExceptionDto(String url, String missingValue) {
        this.url = url;
        this.missingValue = missingValue;
    }
}
