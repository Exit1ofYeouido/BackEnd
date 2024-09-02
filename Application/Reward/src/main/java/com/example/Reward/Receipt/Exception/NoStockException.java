package com.example.Reward.Receipt.Exception;

public class NoStockException extends ReceiptException {
    public NoStockException() {
        super(ReceiptErrorStatus.NO_STOCK);
    }
}
