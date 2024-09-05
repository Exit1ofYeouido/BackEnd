package com.example.Reward.Advertisement.Webclient.Service;


import com.example.Reward.Advertisement.Exception.NotAccessTokenException;
import com.example.Reward.Advertisement.Webclient.OauthInfo;
import com.example.Reward.Advertisement.Webclient.Token;
import com.example.Reward.Advertisement.Webclient.TokenInfo;
import com.example.Reward.Common.Repository.TokenInfoRepository;
import jakarta.transaction.Transactional;
import jdk.swing.interop.SwingInterOpUtils;
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

    @Value("${app.secretkey}")
    private String APPSECRET ;

    public static String ACCESS_TOKEN;

    WebClient client=WebClient.create();

    @Transactional
    public String getAccessToken() {

        Optional<TokenInfo> tokenInfos=tokenInfoRepository.findById(1L);


        if (tokenInfos.isEmpty()) {
            ACCESS_TOKEN = generateAccessToken();
            TokenInfo tokenInfo = TokenInfo
                    .builder()
                    .typeNumber(1L)
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
        OauthInfo bodyOauthInfo=OauthInfo.builder()
                .grant_type("client_credentials")
                .appkey(APPKEY)
                .appsecret(APPSECRET)
                .build();


        Mono<Token> mono = client.post()
                .uri(url)
                .header("content-type", "application/json")
                .bodyValue(bodyOauthInfo)
                .retrieve()
                .bodyToMono(Token.class);

        Token token = mono.block();
        if (token == null) {
            throw new NotAccessTokenException();
        }

        ACCESS_TOKEN = token.getAccess_token();

        return ACCESS_TOKEN;

    }
}
