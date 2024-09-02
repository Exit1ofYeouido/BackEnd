package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;

public class MissintOcrInfoException extends ReceiptException {
    private String url;

    public MissintOcrInfoException(String url) {
        super(ReceiptErrorStatus.MISSING_OCR_INFO);
        this.url = url;
    }
}
