package com.example.Reward.Advertisement.Webclient;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity

public class TokenInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String accessToken;
}
