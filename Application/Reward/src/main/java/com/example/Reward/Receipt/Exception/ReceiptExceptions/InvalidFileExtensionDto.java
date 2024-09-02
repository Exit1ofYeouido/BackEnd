package com.example.Reward.Receipt.Exception.ReceiptExceptions;

import lombok.Getter;

@Getter
public class InvalidFileExtensionDto {
    private String extension;
    private String validExtentions;

    public InvalidFileExtensionDto(String extension, String validExtentions) {
        this.extension = extension;
        this.validExtentions = validExtentions;
    }
}
