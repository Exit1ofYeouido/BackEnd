package com.example.Reward.Advertisement.Webclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthInfo {

    private String grant_type;

    private String appkey;

    private String appsecret;



}
