package com.example.ApiGateWay.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private final SecretKey secretKey;

    public static class Config {
    }

    public AuthorizationHeaderFilter(@Value("${spring.token.secret}") String secret) {
        super(Config.class);
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SIG.HS256.key().build().getAlgorithm());
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();


            String path = exchange.getRequest().getURI().getPath();

            if (path.contains("/v3/api-docs")) {
                return chain.filter(exchange);
            }
            if (path.contains("/signup")){
                return chain.filter(exchange);
            }


            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                if (isJwtExpired(jwt)) {
                    return onError(exchange, "JWT Expired is expired", HttpStatus.FORBIDDEN);
                }
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            String memberId = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload()
                    .get("memberId", String.class);

            exchange.getRequest().mutate().header("memberId", memberId).build();
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(error);

        // 응답 본문에 오류 메시지 추가
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = String.format("{\"error\": \"%s\"}", error);

        DataBufferFactory bufferFactory = response.bufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(responseBody.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(dataBuffer));
    }

    private boolean isJwtValid(String jwt) {
        String subject = null;

        try {
            subject = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt)
                    .getPayload().get("memberId", String.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return !Strings.isBlank(subject);
    }

    private boolean isJwtExpired(String jwt) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload().getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            log.error("Error checking JWT expiration: " + e.getMessage());
            return true;
        }
    }

}
