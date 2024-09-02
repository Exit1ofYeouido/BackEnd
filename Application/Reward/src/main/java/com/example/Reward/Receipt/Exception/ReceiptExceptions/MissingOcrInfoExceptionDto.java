package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import lombok.Getter;

@Getter
public class MissingOcrInfoExceptionDto {
    private String url;
    private String message;

    public MissingOcrInfoExceptionDto(String url, String message) {
        this.url = url;
        this.message = message;
    }
}
