package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;

public class ExistingPathException extends ReceiptException {
    public ExistingPathException() { super(ReceiptErrorStatus.EXISTING_PATH); }
}
