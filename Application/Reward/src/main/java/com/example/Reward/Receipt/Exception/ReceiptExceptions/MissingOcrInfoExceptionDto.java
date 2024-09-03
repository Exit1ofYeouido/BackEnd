package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class MissingOcrInfoExceptionDto {
    private String url;
    private ArrayList<String> missingValue;

    public MissingOcrInfoExceptionDto(String url, ArrayList<String> missingValue) {
        this.url = url;
        this.missingValue = missingValue;
    }
}
