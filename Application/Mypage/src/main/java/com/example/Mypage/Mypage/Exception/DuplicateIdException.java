package com.example.Mypage.Mypage.Exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateIdException extends DataIntegrityViolationException {
    public DuplicateIdException(String message) {
        super(message);
    }
}
