package com.example.Reward.Receipt.Exception;

public class KafkaCommunicationException extends ReceiptException {
    public KafkaCommunicationException() {
        super(ReceiptErrorStatus.KAFKA_COMMUNICATION);
    }
}
