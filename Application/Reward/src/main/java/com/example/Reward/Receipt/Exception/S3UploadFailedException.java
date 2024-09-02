package com.example.Reward.Receipt.Exception;

public class S3UploadFailedException extends ReceiptException {
    public S3UploadFailedException() {
        super(ReceiptErrorStatus.S3_UPLOAD_FAILED);
    }

}
