package com.example.Reward.Receipt.Exception;

public class OcrErrorException extends ReceiptException {
    private String url;

    public OcrErrorException(String url) {
        super(ReceiptErrorStatus.OCR_ERROR);
        this.url = url;
    }
}
