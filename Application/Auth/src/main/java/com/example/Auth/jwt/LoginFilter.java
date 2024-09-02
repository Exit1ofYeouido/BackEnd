package com.example.Auth.jwt;

import com.example.Auth.dto.LoginRequestDTO;
import com.example.Auth.entity.MemberAuth;
import com.example.Auth.entity.RefreshToken;
import com.example.Auth.repository.AuthRepository;
import com.example.Auth.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AuthRepository authRepository;
    private final TokenService tokenService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, ObjectMapper objectMapper,
                       AuthRepository authRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.authRepository = authRepository;
        this.tokenService = tokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            LoginRequestDTO loginRequestDTO = objectMapper.readValue(request.getInputStream(),
                    LoginRequestDTO.class);
            String username = loginRequestDTO.getMemberName();
            String password = loginRequestDTO.getMemberPassword();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            log.error(e.getMessage());
            //TODO : ExceptionHandler 처리하기
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authToken) throws IOException, ServletException {

        String username = authToken.getName();

        Collection<? extends GrantedAuthority> authorities = authToken.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();
        String role = grantedAuthority.getAuthority();

        MemberAuth memberAuth = authRepository.findByMemberName(username).orElse(null);
        String memberId = memberAuth.getMemberId().toString();

        String access = jwtUtil.createJwt("access", memberId, role, TokenExpiration.ACCESS);
        String refresh = jwtUtil.createJwt("refresh", memberId, role, TokenExpiration.REFRESH);

        addRefreshToken(memberId, refresh);

        response.setHeader("accessToken", access);
        response.addCookie(createCookie("refreshToken", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.info("로그인 실패");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 3);
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshToken(String memberId, String refreshToken) {

        RefreshToken refresh = RefreshToken.builder()
                .id(memberId)
                .token(refreshToken)
                .build();

        if (tokenService.saveRefreshToken(refresh)) {
            log.info("Refresh token successful saved");
        } else {
            log.info("Refresh token failed to save");
        }

    }
}
