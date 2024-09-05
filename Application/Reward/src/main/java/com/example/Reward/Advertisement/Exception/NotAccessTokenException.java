package com.example.Reward.Advertisement.Exception;

public class NotAccessTokenException extends AdException{
    public NotAccessTokenException() {
        super(AdStatus.NotMatched);
    }
}
