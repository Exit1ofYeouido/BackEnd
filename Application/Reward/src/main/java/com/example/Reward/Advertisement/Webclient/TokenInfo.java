package com.example.Reward.Advertisement.Webclient;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {


    @Id
    private Long typeNumber;

    @Column(name="token_value",columnDefinition = "TEXT")
    private String accessToken;
}
