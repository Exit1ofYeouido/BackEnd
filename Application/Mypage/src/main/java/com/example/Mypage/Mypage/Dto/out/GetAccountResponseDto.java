package com.example.Mypage.Mypage.Dto.out;


import lombok.Getter;

@Getter
public class GetAccountResponseDto {

    private String accountName;

    private String accountId;

    public GetAccountResponseDto(){
        this.accountName="신한투자증권 S-Lite ";
        this.accountId="270-534-290-59";

    }
}
