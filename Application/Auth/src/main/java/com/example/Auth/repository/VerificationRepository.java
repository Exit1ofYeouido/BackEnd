package com.example.Auth.repository;

import com.example.Auth.entity.PhoneVerification;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VerificationRepository {
    private final int EXPIRATION_TIME = 120;
    private final RedisTemplate<String, PhoneVerification> redisTemplate;

    public PhoneVerification findByPhoneNumber(String phoneNumber) {
        return redisTemplate.opsForValue().get(phoneNumber);
    }

    public void save(PhoneVerification phoneVerification) {
        redisTemplate.opsForValue().set(phoneVerification.getPhoneNumber(),
                phoneVerification);

        redisTemplate.expire(phoneVerification.getPhoneNumber(), EXPIRATION_TIME, TimeUnit.SECONDS);
        //TODO:인증번호까지 저장한 객체를 만들어서 저장하기
    }

    public Long getExpireTime(String phoneNumber) {
        return redisTemplate.getExpire(phoneNumber, TimeUnit.SECONDS);
    }

}
