package com.example.Reward.Receipt.Exception;

public class MissintOcrInfoException extends ReceiptException {
    private String url;

    public MissintOcrInfoException(String url) {
        super(ReceiptErrorStatus.MISSING_OCR_INFO);
        this.url = url;
    }
}
