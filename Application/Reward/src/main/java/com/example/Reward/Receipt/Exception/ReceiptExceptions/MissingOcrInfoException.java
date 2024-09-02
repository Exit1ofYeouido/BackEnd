package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;

public class MissingOcrInfoException extends ReceiptException {
    private String url;

    public MissingOcrInfoException(String url) {
        super(ReceiptErrorStatus.MISSING_OCR_INFO);
        this.url = url;
    }
}
