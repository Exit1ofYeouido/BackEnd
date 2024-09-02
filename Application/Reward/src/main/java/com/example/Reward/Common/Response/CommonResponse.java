package com.example.Reward.Common.Response;

import lombok.Getter;

@Getter
public class CommonResponse {
    private final int status;
    private final String message;

    public CommonResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
