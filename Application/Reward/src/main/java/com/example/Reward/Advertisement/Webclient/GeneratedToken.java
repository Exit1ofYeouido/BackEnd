package com.example.Reward.Advertisement.Webclient;


import com.example.Reward.Common.Repository.TokenInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneratedToken {

    private final TokenInfoRepository tokenInfoRepository;

    @Value("${app.key}")
    public static String APPKEY ;

    @Value("${app.secretkey}")
    public static String APPSECRET ;

    WebClient client=WebClient.create();

    public String getAccessToken() {

        List<TokenInfo> tokenInfos=tokenInfoRepository.findAll();
        String ACCESS_TOKEN=tokenInfos.get(0).getAccessToken();
        if (ACCESS_TOKEN == null) {
            ACCESS_TOKEN = generateAccessToken();
        }

        return ACCESS_TOKEN;
    }

    public String generateAccessToken(){

        String url = "https://openapi.koreainvestment.com:9443" + "/oauth2/tokenP";
        Body body=new Body(APPKEY,APPSECRET,"client_credentials");

        Mono<TokenInfo> mono = client.post()
                .uri(url)
                .header("content-type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(TokenInfo.class);

        TokenInfo tokenInfo = mono.block();

        if (tokenInfo == null) {
            throw new RuntimeException("액세스 토큰을 가져올 수 없습니다.");
        }

        String ACCESS_TOKEN = tokenInfo.getAccessToken();

        return ACCESS_TOKEN;

    }
}
