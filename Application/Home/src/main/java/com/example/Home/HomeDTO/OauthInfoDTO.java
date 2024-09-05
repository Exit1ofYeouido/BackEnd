package com.example.Home.HomeDTO;

import lombok.Builder;
import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthInfoDTO {

    private String grant_type;

    private String appkey;

    private String appsecret;

}