package com.example.Reward.Advertisement.Exception;

public class NoStockException extends AdException {

    public NoStockException(){
        super(AdStatus.NoStock);
    }
}
