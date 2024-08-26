package com.example.Auth.service;

import com.example.Auth.entity.RefreshToken;
import com.example.Auth.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public boolean saveRefreshToken(RefreshToken refreshToken) {
        tokenRepository.save(refreshToken);
        //TODO: 성공적으로 저장되었는지 처리하는 로직 세부화
        return true;
    }
}
