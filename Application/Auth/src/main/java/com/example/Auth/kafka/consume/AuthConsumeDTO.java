package com.example.Auth.kafka.consume;

import com.example.Auth.entity.MemberAuth;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonSerialize
@JsonDeserialize
@AllArgsConstructor
@NoArgsConstructor
public class AuthConsumeDTO {

    private Long memberId;
    private String memberName;
    private String memberPassword;
    private String phoneNumber;
    private String role;

    public MemberAuth toEntity() {
        MemberAuth memberAuth = MemberAuth.builder()
                .memberId(memberId)
                .memberName(memberName)
                .memberPassword(memberPassword)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
        return memberAuth;
    }
}
