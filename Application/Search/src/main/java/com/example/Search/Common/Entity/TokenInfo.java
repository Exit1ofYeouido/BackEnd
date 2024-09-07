package com.example.Search.Common.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    @Column(name = "type_number")
    private Long id;

    @Setter
    @Column(name = "token_value", columnDefinition = "TEXT")
    private String accessToken;

}