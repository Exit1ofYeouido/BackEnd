package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;

public class GiveStockErrorException extends ReceiptException {
    public GiveStockErrorException() {
        super(ReceiptErrorStatus.GIVE_STOCK_ERROR);
    }
}
