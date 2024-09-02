package com.example.Reward.Receipt.Exception;

public class GiveStockErrorException extends ReceiptException {
    public GiveStockErrorException() {
        super(ReceiptErrorStatus.GIVE_STOCK_ERROR);
    }
}
