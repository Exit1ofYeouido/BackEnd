package com.example.Reward.Receipt.Exception;

public class InvalidFileExtensionException extends ReceiptException{
    private final String extension;
    private static final String correntExtentions = "jpg, jpeg, png, svg";

    public InvalidFileExtensionException(String extension) {
        super(ReceiptErrorStatus.INVALID_FILE_EXTENSION);
        this.extension = extension;
    }
}
