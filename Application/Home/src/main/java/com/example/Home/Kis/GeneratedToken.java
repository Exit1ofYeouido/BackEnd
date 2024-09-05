package com.example.Home.Kis;

import com.example.Home.Common.Entity.Token;
import com.example.Home.Common.Entity.TokenInfo;
import com.example.Home.Common.Repository.TokenInfoRepository;
import com.example.Home.HomeDTO.OauthInfoDTO;
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

    @Value("${APP_KEY}")
    private String APPKEY ;

    @Value("${APP_SECRETKEY}")
    private String APPSECRET ;

    public static String ACCESS_TOKEN;

    WebClient client=WebClient.create();

    public String getAccessToken() {

        Optional<TokenInfo> tokenInfos=tokenInfoRepository.findById(1L);

        if (tokenInfos.isEmpty()) {
            ACCESS_TOKEN = generateAccessToken();
            System.out.println(ACCESS_TOKEN);
            TokenInfo tokenInfo = TokenInfo
                    .builder()
                    .typeNumber(1L)
                    .tokenValue(ACCESS_TOKEN)
                    .build();
            tokenInfoRepository.save(tokenInfo);
            return ACCESS_TOKEN;
        }
        String Is_ACCESS_TOKEN = tokenInfos.get().getTokenValue();

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
