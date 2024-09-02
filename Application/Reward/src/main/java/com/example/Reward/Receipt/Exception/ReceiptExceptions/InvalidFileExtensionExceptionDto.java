package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import lombok.Getter;

@Getter
public class InvalidFileExtensionExceptionDto {
    private String extension;
    private String validExtentions;

    public InvalidFileExtensionExceptionDto(String extension, String validExtentions) {
        this.extension = extension;
        this.validExtentions = validExtentions;
    }
}
