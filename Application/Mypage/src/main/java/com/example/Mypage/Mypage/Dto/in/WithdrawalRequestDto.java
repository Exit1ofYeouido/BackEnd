package com.example.Mypage.Mypage.Dto.in;

import lombok.Data;

@Data
public class WithdrawalRequestDto {

    private int withdrawalAmount;

    private String accountNumber;
}
