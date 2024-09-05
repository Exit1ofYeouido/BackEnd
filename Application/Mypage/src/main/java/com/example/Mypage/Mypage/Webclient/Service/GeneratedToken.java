package com.example.Mypage.Mypage.Webclient.Service;



import com.example.Mypage.Mypage.Exception.NotFoundTokenException;
import com.example.Mypage.Mypage.Webclient.Entity.ApprovalKey;
import com.example.Mypage.Mypage.Webclient.Entity.TokenInfo;
import com.example.Mypage.Mypage.Webclient.Dto.OauthInfoDto;
import com.example.Mypage.Mypage.Webclient.Repository.TokenInfoRepository;
import com.example.Mypage.Mypage.Webclient.Entity.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeneratedToken {

    private final TokenInfoRepository tokenInfoRepository;

    @Value("${app.key}")
    private String APPKEY ;

    @Value("${app.secretkey}")
    private String APPSECRET ;

    public static String ACCESS_TOKEN;
    public static String SOCKET_TOKEN;

    WebClient client=WebClient.create();

    public String getAccessToken() {

        Optional<TokenInfo> tokenInfos=tokenInfoRepository.findById(1L);

        if (tokenInfos.isEmpty()) {
            ACCESS_TOKEN = generateAccessToken();
            TokenInfo tokenInfo = TokenInfo
                    .builder()
                    .tokenValue(ACCESS_TOKEN)
                    .id(1L)
                    .build();
            tokenInfoRepository.save(tokenInfo);
            return ACCESS_TOKEN;
        }

        String Is_ACCESS_TOKEN = tokenInfos.get().getTokenValue();

        return Is_ACCESS_TOKEN;
    }

    public String getSocketToken() {
        TokenInfo tokenInfos = tokenInfoRepository.findById(2L).orElseThrow(() -> new NotFoundTokenException("소켓 통신 접근키를 찾을 수 없습니다."));

        String APPROVAL_TOKEN = tokenInfos.getTokenValue();

        return APPROVAL_TOKEN;
    }

    public String generateAccessToken(){

        String url = "https://openapi.koreainvestment.com:9443" + "/oauth2/tokenP";
        OauthInfoDto bodyOauthInfoDto = OauthInfoDto.builder()
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

    public String updateSocketToken(){

        String url = "https://openapi.koreainvestment.com:9443/oauth2/Approval";

        OauthInfoDto bodyOauthInfoDto = OauthInfoDto.builder()
                .grant_type("client_credentials")
                .appkey(APPKEY)
                .secretkey(APPSECRET)
                .build();

        Mono<ApprovalKey> mono = client.post()
                .uri(url)
                .header("content-type", "application/json")
                .bodyValue(bodyOauthInfoDto)
                .retrieve()
                .bodyToMono(ApprovalKey.class);

        ApprovalKey approvalKey = mono.block();
        if (approvalKey == null) {
            throw new RuntimeException("소켓 접속키를 가져올 수 없습니다.");
        }

        SOCKET_TOKEN = approvalKey.getApproval_key();

        TokenInfo tokenInfo = tokenInfoRepository.findById(2L).orElse(null);
        tokenInfo.setTokenValue(SOCKET_TOKEN);
        tokenInfoRepository.save(tokenInfo);

        return SOCKET_TOKEN;
    }
}
