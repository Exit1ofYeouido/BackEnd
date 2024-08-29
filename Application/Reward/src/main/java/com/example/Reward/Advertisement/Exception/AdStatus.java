package com.example.Reward.Advertisement.Exception;

import lombok.Getter;

@Getter
public enum AdStatus {

      BASIC(400, "기본상태 코드"),
      NoStock(400,"주식이 없습니다!"),
      NotMatched(400,"기업이 일치하지않습니다!");


    private final int status;
    private final String message;

    AdStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
