package com.example.ApiGateWay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .formLogin(formLoginSpec -> formLoginSpec.disable())
                .httpBasic(httpBasicSpec -> httpBasicSpec.authenticationEntryPoint(new HttpStatusServerEntryPoint(
                        HttpStatus.UNAUTHORIZED)))
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**",
                                "/api/basic/**", "/swagger-ui.html", "/api/auth/**", "/error", "/api/swagger-ui/**")
                        .permitAll()
                        .anyExchange().authenticated()
                );

        return http.build();
    }
}

