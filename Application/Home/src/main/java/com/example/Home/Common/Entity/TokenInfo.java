package com.example.Home.Common.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {

    @Id
    private Long typeNumber;

    @Column(columnDefinition = "TEXT")
    private String tokenValue;

}