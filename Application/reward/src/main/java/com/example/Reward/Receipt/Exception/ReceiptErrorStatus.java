package com.example.Reward.Receipt.Exception;

import lombok.Getter;

@Getter
public enum ReceiptErrorStatus {
    NO_STOCK(400, "주식이 없습니다!"),
    INVALID_FILE_EXTENSION(401, "확장자가 올바르지 않습니다!"),
    S3_UPLOAD_FAILED(402, "S3 업로드 실패!"),
    OCR_ERROR(403, "OCR 요청 오류!"),
    MISSING_OCR_INFO(404, "영수증 내 필수 정보 누락!"),
    STOCK_NOT_FOUND(405, "받을 수 있는 주식이 없습니다!"),
    KIS_API(406, "한국투자증권 API 오류!"),
    GIVE_STOCK_ERROR(407, "주식 증정 로직 오류!"),
    EXISTING_RECEIPT(408, "이미 사용된 영수증입니다!");

    private final int status;
    private final String message;

    ReceiptErrorStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
