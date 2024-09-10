package com.example.Home.Kis;

import com.example.Home.Common.Entity.Token;
import com.example.Home.Common.Entity.TokenInfo;
import com.example.Home.Common.Repository.TokenInfoRepository;
import com.example.Home.HomeDTO.OauthInfoDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GeneratedToken {

    private final TokenInfoRepository tokenInfoRepository;

    @Value("${app.key}")
    private String APPKEY;

    @Value("${app.secret}")
    private String APPSECRET;

    @Value("${app.fourth-key}")
    private String FOURTH_API_KEY;

    @Value("${app.fourth-secret}")
    private String FOURTH_API_SECRET;


    public static String ACCESS_TOKEN;

    WebClient client = WebClient.create();

    public String getAccessToken(Long tokenId) {

        Optional<TokenInfo> tokenInfos = tokenInfoRepository.findById(tokenId);

        if (tokenInfos.isEmpty()) {
            if (tokenId == 6L) {
                ACCESS_TOKEN = generateAccessToken(APPKEY, APPSECRET);
            } else {
                ACCESS_TOKEN = generateAccessToken(FOURTH_API_KEY, FOURTH_API_SECRET);
            }

            TokenInfo tokenInfo = TokenInfo
                    .builder()
                    .typeNumber(tokenId)
                    .tokenValue(ACCESS_TOKEN)
                    .build();
            tokenInfoRepository.save(tokenInfo);
            return ACCESS_TOKEN;
        }
        String Is_ACCESS_TOKEN = tokenInfos.get().getTokenValue();

        return Is_ACCESS_TOKEN;
    }

    public String generateAccessToken(String app, String secret) {

        String url = "https://openapi.koreainvestment.com:9443" + "/oauth2/tokenP";
        OauthInfoDTO bodyOauthInfoDto = OauthInfoDTO.builder()
                .grant_type("client_credentials")
                .appkey(app)
                .appsecret(secret)
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
