package com.example.Reward.Receipt.Exception;

import com.example.Reward.Common.Response.CommonResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReceiptErrorResponse extends CommonResponse {
    private final Object data;

    @Builder
    public ReceiptErrorResponse(int status, String message, Object data) {
        super(status, message);
        this.data = data;
    }

    public static ReceiptErrorResponse error(String message) {
        return new ReceiptErrorResponse(400, message, null);
    }
}
