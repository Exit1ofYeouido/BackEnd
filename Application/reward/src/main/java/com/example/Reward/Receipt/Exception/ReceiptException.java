package com.example.Reward.Receipt.Exception;

public class ReceiptException extends RuntimeException {
    private final ReceiptErrorStatus receiptErrorStatus;

    public ReceiptException(ReceiptErrorStatus receiptErrorStatus) {
        this.receiptErrorStatus = receiptErrorStatus;
    }

    public int getStatus() {
        return receiptErrorStatus.getStatus();
    }

    public String getMessage() {
        return receiptErrorStatus.getMessage();
    }
}
