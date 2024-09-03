package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class MissingOcrInfoException extends ReceiptException {
    private String url;
    private ArrayList<String> missingValue;

    public MissingOcrInfoException(String url, ArrayList<String> missingValue) {
        super(ReceiptErrorStatus.MISSING_OCR_INFO);
        this.url = url;
        this.missingValue = missingValue;
    }
}
