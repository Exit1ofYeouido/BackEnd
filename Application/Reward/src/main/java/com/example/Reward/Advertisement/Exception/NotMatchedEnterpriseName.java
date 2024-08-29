package com.example.Reward.Advertisement.Exception;

import lombok.Getter;

@Getter
public class NotMatchedEnterpriseName extends  AdException{

    private String wrongName;

    private String currentName;

    public NotMatchedEnterpriseName(String wrongName,String currentName){
        super(AdStatus.BASIC);
        this.wrongName=wrongName;
        this.currentName=currentName;

    }
}
