package com.example.Mypage.Mypage.Webclient;



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
    private String APPKEY ;

    @Value("${app.secretkey}")
    private String APPSECRET ;

    public static String ACCESS_TOKEN;

    WebClient client=WebClient.create();

    public String getAccessToken() {

        List<TokenInfo> tokenInfos=tokenInfoRepository.findAll();

        if (tokenInfos.isEmpty()) {
            ACCESS_TOKEN = generateAccessToken();
            System.out.println(ACCESS_TOKEN);
            TokenInfo tokenInfo = TokenInfo
                    .builder()
                    .accessToken(ACCESS_TOKEN)
                    .build();
            tokenInfoRepository.save(tokenInfo);
            return ACCESS_TOKEN;
        }
        String Is_ACCESS_TOKEN = tokenInfos.get(0).getAccessToken();

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
            throw new RuntimeException("액세스 토큰을 가져올 수 없습니다.");
        }

        ACCESS_TOKEN = token.getAccess_token();

        return ACCESS_TOKEN;

    }
}
