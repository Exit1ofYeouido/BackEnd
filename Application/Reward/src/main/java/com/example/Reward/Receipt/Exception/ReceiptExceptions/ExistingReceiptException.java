package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;
import lombok.Getter;

@Getter
public class ExistingReceiptException extends ReceiptException {
    private String url;

    public ExistingReceiptException(String url) {
        super(ReceiptErrorStatus.EXISTING_RECEIPT);
        this.url = url;
    }
}
