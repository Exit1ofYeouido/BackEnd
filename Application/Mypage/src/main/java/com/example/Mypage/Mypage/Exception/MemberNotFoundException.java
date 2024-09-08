package com.example.Mypage.Mypage.Exception;

import java.util.NoSuchElementException;

public class MemberNotFoundException extends NoSuchElementException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
