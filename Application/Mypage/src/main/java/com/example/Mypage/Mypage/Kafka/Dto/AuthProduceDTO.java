package com.example.Mypage.Mypage.Kafka.Dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@JsonSerialize
@JsonDeserialize
@NoArgsConstructor
@AllArgsConstructor
public class AuthProduceDTO {

    private Long memberId;
    private String memberName;
    private String memberPassword;
    private String phoneNumber;
    private String role;

}
