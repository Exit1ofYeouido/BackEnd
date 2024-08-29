package com.example.Auth.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@RedisHash(value = "token")
public class RefreshToken {

    @Id
    private String id;

    @Indexed
    private String token;
}
