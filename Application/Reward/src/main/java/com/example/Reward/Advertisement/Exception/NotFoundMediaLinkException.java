package com.example.Reward.Advertisement.Exception;

public class NotFoundMediaLinkException extends AdException {
    public NotFoundMediaLinkException(){
        super(AdStatus.NotMatchedMediaLink);
    }
}
