package com.example.Mypage.Mypage.Exception;

public class BadRequestException extends NullPointerException {
    public BadRequestException(String message) {
        super(message);
    }
}
