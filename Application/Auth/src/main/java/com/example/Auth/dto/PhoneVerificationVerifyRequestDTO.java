package com.example.Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneVerificationVerifyRequestDTO {
    private String phoneNumber;

    private String verificationCode;

    public com.example.Auth.entity.PhoneVerification toEntity() {
        com.example.Auth.entity.PhoneVerification phoneVerification = com.example.Auth.entity.PhoneVerification.builder()
                .phoneNumber(phoneNumber)
                .verificationCode(verificationCode)
                .build();

        return phoneVerification;
    }
}
