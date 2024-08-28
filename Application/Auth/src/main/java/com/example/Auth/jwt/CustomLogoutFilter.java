package com.example.Auth.jwt;

import com.example.Auth.entity.RefreshToken;
import com.example.Auth.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        if (!requestURI.matches("^\\/logout$") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            log.info("이미 만료된 토큰입니다.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String memberId = jwtUtil.getMemberId(refreshToken);
        RefreshToken savedToken = tokenRepository.findById(memberId);

        if (savedToken != null) {
            tokenRepository.deleteById(memberId);
        }

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        log.info("memberID : {} -> 로그아웃 완료", memberId);

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
