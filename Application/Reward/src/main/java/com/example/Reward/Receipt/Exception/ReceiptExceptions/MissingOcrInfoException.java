package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;

public class MissingOcrInfoException extends ReceiptException {
    private String url;
    private String missingValue;
    private String missingMessage;

    public MissingOcrInfoException(String missingValue, String url) {
        super(ReceiptErrorStatus.MISSING_OCR_INFO);
        this.url = url;
        this.missingValue = missingValue;
        this.missingMessage = "누락된 정보 : " + missingValue;
    }
}
