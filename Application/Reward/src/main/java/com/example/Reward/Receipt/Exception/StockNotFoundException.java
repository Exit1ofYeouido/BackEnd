package com.example.Reward.Receipt.Exception;

public class StockNotFoundException extends ReceiptException {
    private String foundName;

    public StockNotFoundException(String foundName) {
        super(ReceiptErrorStatus.STOCK_NOT_FOUND);
        this.foundName = foundName;
    }
}
