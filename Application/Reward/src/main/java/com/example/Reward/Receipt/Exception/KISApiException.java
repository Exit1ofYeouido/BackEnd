package com.example.Reward.Receipt.Exception;

public class KISApiException extends ReceiptException {
    public KISApiException() {
        super(ReceiptErrorStatus.KIS_API);
    }
}
