package com.example.Auth.jwt;

public enum TokenExpiration {
    ACCESS(1000 * 60 * 60 * 3L), // 3시간
    REFRESH(1000 * 60 * 60 * 72L); // 72시간

    private final long expiredTime;

    TokenExpiration(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    @Override
    public String toString() {
        return String.valueOf(expiredTime);
    }
}
