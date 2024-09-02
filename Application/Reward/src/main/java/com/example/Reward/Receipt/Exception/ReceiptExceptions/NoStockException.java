package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;
import lombok.Getter;

@Getter
public class NoStockException extends ReceiptException {
    public NoStockException() {
        super(ReceiptErrorStatus.NO_STOCK);
    }
}
