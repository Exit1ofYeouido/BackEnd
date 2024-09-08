package com.example.Mypage.Mypage.Exception;

public class ExceedSaleAmountException extends IllegalArgumentException {
    public ExceedSaleAmountException(String message) {
        super(message);
    }
}