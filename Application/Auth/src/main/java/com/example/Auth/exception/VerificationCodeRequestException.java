package com.example.Auth.exception;

public class VerificationCodeRequestException extends IllegalStateException {
    public VerificationCodeRequestException(String message) {
        super(message);
    }
}
