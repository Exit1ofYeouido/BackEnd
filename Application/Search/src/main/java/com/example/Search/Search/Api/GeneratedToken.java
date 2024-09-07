package com.example.Search.Search.Api;


import com.example.Search.Common.Entity.Token;
import com.example.Search.Common.Entity.TokenInfo;
import com.example.Search.Common.Repository.TokenInfoRepository;
import com.example.Search.Search.SearchDTO.OauthInfoDTO;
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
            ACCESS_TOKEN = generateAccessToken();

            TokenInfo tokenInfo = TokenInfo
                    .builder()
                    .id(tokenId)
                    .accessToken(ACCESS_TOKEN)
                    .build();

            tokenInfoRepository.save(tokenInfo);

            return ACCESS_TOKEN;
        }
        String Is_ACCESS_TOKEN = tokenInfos.get().getAccessToken();

        return Is_ACCESS_TOKEN;
    }

    public String generateAccessToken() {

        String url = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";
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

    public String generateFourthAccessToken() {

        String url = "https://openapi.koreainvestment.com:9443" + "/oauth2/tokenP";
        OauthInfoDTO bodyOauthInfoDto = OauthInfoDTO.builder()
                .grant_type("client_credentials")
                .appkey(FOURTH_API_KEY)
                .appsecret(FOURTH_API_SECRET)
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

        return ACCESS_TOKEN = token.getAccess_token();
    }
}