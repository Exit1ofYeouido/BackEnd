package com.example.Auth.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Setter
@NoArgsConstructor
@RedisHash(value = "token", timeToLive = 604_800)
public class RefreshToken {

    @Id
    private String id;

    @Indexed
    private String token;
}
