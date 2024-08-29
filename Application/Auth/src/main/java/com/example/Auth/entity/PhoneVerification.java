package com.example.Auth.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneVerification {
    @Id
    private String phoneNumber;

    @Indexed
    private String verificationCode;

}
