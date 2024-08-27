package com.example.Auth.repository;

import com.example.Auth.entity.RefreshToken;
import com.example.Auth.jwt.TokenExpiration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final RedisTemplate<String, RefreshToken> redisTemplate;

    public RefreshToken findById(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void save(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getId(), refreshToken);
        redisTemplate.expire(refreshToken.getId(), TokenExpiration.REFRESH.getExpiredTime(), TimeUnit.SECONDS);
    }
}
