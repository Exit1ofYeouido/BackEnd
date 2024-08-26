package com.example.Auth.jwt;

import com.example.Auth.dto.LoginRequestDTO;
import com.example.Auth.entity.MemberAuth;
import com.example.Auth.repository.AuthRepository;
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

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, ObjectMapper objectMapper,
                       AuthRepository authRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.authRepository = authRepository;
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

        //MemberId 가져오기
        MemberAuth memberAuth = authRepository.findByMemberName(username).orElse(null);
        String memberId = memberAuth.getMemberId().toString();

        //토큰 생성
        String access = jwtUtil.createJwt("access", memberId, role, 1000 * 60 * 60 * 12L);
        String refresh = jwtUtil.createJwt("refresh", memberId, role, 1000 * 60 * 60 * 72L);

        //Refresh 토큰 저장
        //addRefreshToken(username, refresh, 1000 * 60 * 24L);

        //응답 생성
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
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
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);

        return cookie;
    }

//    private void addRefreshToken(String username, String refreshToken, Long expiredMs) {
//
//        Date date = new Date(System.currentTimeMillis() + expiredMs);
//
//        Refresh refresh = new Refresh();
//        refresh.setUsername(username);
//        refresh.setRefresh(refreshToken);
//        refresh.setExpiration(date.toString());
//
//        RedisRefresh redisRefresh = new RedisRefresh();
//        redisRefresh.setId(username);
//        redisRefresh.setToken(refreshToken);
//        redisRefresh.setTtl(3600L);
//
//        refreshRepository.save(refresh);
//        tokenRedisRepository.save(redisRefresh);
//
//    }
}
