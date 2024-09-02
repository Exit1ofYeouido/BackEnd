package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;

public class OcrErrorException extends ReceiptException {
    private String url;

    public OcrErrorException(String url) {
        super(ReceiptErrorStatus.OCR_ERROR);
        this.url = url;
    }
}
