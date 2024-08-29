package com.example.Mypage.Mypage.Webclient.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthInfoDto {

    private String grant_type;

    private String appkey;

    private String appsecret;



}
