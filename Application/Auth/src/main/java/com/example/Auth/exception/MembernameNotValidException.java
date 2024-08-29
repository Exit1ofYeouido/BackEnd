package com.example.Auth.exception;

public class MembernameNotValidException extends IllegalArgumentException {
    public MembernameNotValidException() {
        super("유저 아이디는 최소 4자리 이상 또는 숫자로만 구성할 수 없습니다.");
    }
}
