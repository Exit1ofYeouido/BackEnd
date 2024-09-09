package com.example.Search.Exception;

public class NotAdminException extends IllegalStateException {
    public NotAdminException(String message) {
        super(message);
    }
}
