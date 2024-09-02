package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import com.example.Reward.Receipt.Exception.ReceiptErrorStatus;
import com.example.Reward.Receipt.Exception.ReceiptException;

public class S3UploadFailedException extends ReceiptException {
    public S3UploadFailedException() {
        super(ReceiptErrorStatus.S3_UPLOAD_FAILED);
    }

}
