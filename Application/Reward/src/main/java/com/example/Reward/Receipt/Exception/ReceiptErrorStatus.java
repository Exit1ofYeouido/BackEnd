package com.example.Reward.Receipt.Exception;

import lombok.Getter;

@Getter
public enum ReceiptErrorStatus {
    NO_STOCK(400, "주식이 없습니다 !!"),
    INVALID_FILE_EXTENSION(401, "올바르지 않은 파일 확장자입니다 !!"),
    S3_UPLOAD_FAILED(402, "S3 업로드 실패 !!"),
    OCR_ERROR(403, "해당 사진에 대한 OCR 요청 오류 !!"),
    MISSING_OCR_INFO(404, "영수증 내 필수 정보 누락 !!"),
    STOCK_NOT_FOUND(405, "해당 이름으로 상장된 주식이 없습니다 !!"),
    KIS_API(406, "한국투자증권 API 오류 !!"),
    GIVE_STOCK_ERROR(407, "주식 증정 로직 오류 !!");

    private final int status;
    private final String message;

    ReceiptErrorStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
