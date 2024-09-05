package com.example.Mypage.Mypage.Exception;

import java.util.NoSuchElementException;

public class NotFoundTokenException extends NoSuchElementException {
    public NotFoundTokenException(String message) {
        super(message);
    }
}
