package com.example.Search.Search.Api;


import com.example.Search.Common.Entity.Token;
import com.example.Search.Common.Entity.TokenInfo;
import com.example.Search.Common.Repository.TokenInfoRepository;
import com.example.Search.Search.SearchDTO.OauthInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GeneratedToken {

    private final TokenInfoRepository tokenInfoRepository;

    @Value("${app.key}")
    private String APPKEY ;

    @Value("${app.secret}")
    private String APPSECRET ;

    public static String ACCESS_TOKEN;

    WebClient client=WebClient.create();

    public String getAccessToken() {

        Optional<TokenInfo> tokenInfos=tokenInfoRepository.findById(1L);

        if (tokenInfos.isEmpty()) {
            ACCESS_TOKEN = generateAccessToken();

            TokenInfo tokenInfo = TokenInfo
                    .builder()
                    .id(1L)
                    .accessToken(ACCESS_TOKEN)
                    .build();

            tokenInfoRepository.save(tokenInfo);

            return ACCESS_TOKEN;
        }
        String Is_ACCESS_TOKEN = tokenInfos.get().getAccessToken();

        return Is_ACCESS_TOKEN;
    }

    public String generateAccessToken(){

        String url = "https://openapi.koreainvestment.com:9443" + "/oauth2/tokenP";
        OauthInfoDTO bodyOauthInfoDto = OauthInfoDTO.builder()
                .grant_type("client_credentials")
                .appkey(APPKEY)
                .appsecret(APPSECRET)
                .build();

        Mono<Token> mono = client.post()
                .uri(url)
                .header("content-type", "application/json")
                .bodyValue(bodyOauthInfoDto)
                .retrieve()
                .bodyToMono(Token.class);

        Token token = mono.block();
        if (token == null) {
            throw new RuntimeException("액세스 토큰을 가져올 수 없습니다.");
        }

        ACCESS_TOKEN = token.getAccess_token();

        return ACCESS_TOKEN;

    }
}