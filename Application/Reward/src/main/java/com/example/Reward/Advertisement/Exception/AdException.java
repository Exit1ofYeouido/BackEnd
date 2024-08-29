package com.example.Reward.Advertisement.Exception;

public class AdException extends RuntimeException {
    private final AdStatus adStatus;

    public AdException(AdStatus adStatus) {
        this.adStatus = adStatus;
    }

    public int getStatus() {
        return adStatus.getStatus();
    }

    public String getMessage() {
        return adStatus.getMessage();
    }
}
