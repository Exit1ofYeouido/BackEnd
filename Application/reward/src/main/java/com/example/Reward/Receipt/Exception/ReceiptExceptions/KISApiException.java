package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;
import lombok.Getter;

@Getter
public class KISApiException extends ReceiptException {
    public KISApiException() {
        super(ReceiptErrorStatus.KIS_API);
    }
}
