package com.example.Mypage.Mypage.Webclient.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {

    @Id
    private Long typeNumber;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String tokenValue;
}
