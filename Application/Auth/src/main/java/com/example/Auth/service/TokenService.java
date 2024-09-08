package com.example.Auth.service;

import com.example.Auth.entity.RefreshToken;
import com.example.Auth.jwt.JWTUtil;
import com.example.Auth.jwt.TokenExpiration;
import com.example.Auth.repository.TokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JWTUtil jwtUtil;

    public boolean saveRefreshToken(RefreshToken refreshToken) {
        tokenRepository.save(refreshToken);
        //TODO: 성공적으로 저장되었는지 처리하는 로직 세부화
        return true;
    }

    public HttpServletResponse reissueRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw e;
        }

        String memberId = jwtUtil.getMemberId(refreshToken);
        String role = jwtUtil.getMemberRole(refreshToken);

        RefreshToken savedRefreshToken = tokenRepository.findById(memberId);

        if (!isValidRefreshToken(refreshToken, savedRefreshToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return response;
        }

        String newAccessToken = jwtUtil.createJwt("access", memberId, role, TokenExpiration.ACCESS);
        String newRefreshToken = jwtUtil.createJwt("refresh", memberId, role, TokenExpiration.REFRESH);

        updateRefreshToken(memberId, newRefreshToken);

        response.setHeader("accssToken", newAccessToken);
        response.addCookie(createCookie("refreshToken", newRefreshToken));
        response.setStatus(HttpServletResponse.SC_CREATED);

        return response;
    }

    private void updateRefreshToken(String memberId, String refreshToken) {

        RefreshToken refresh = RefreshToken.builder()
                .id(memberId)
                .token(refreshToken)
                .build();

        // TODO : 정상적으로 저장되었는지 확인
        saveRefreshToken(refresh);
    }

    private boolean isValidRefreshToken(String refreshToken, RefreshToken savedRefreshToken) {
        if (refreshToken.equals(savedRefreshToken.getToken())) {
            return true;
        }
        return false;
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 3);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
