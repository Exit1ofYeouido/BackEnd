package com.example.Mypage.Mypage.Webclient.Service;

import com.example.Mypage.Mypage.Webclient.Dto.ResponseDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final GeneratedToken generatedToken;
    public static final String REST_BASE_URL = "https://openapi.koreainvestment.com:9443";

    WebClient client=WebClient.create();

    @Value("${app.key}")
    private String APPKEY;

    @Value("${app.secretkey}")
    private String APPSECRET;

    public Integer getPrice(String code) {

        String url=REST_BASE_URL+"/uapi/domestic-stock/v1/quotations/inquire-price?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD="+code;
        Mono<ResponseDto> response=client.get()
                .uri(url)
                .header("authorization","Bearer " + generatedToken.getAccessToken())
                .header("appkey",APPKEY)
                .header("appsecret",APPSECRET)
                .header("tr_id","FHKST01010100")
                .retrieve()
                .bodyToMono(ResponseDto.class);

        ResponseDto result=response.block();

        /** 예외 로직**/

        int cost = Integer.parseInt(result.getOutput().getStck_prpr());

        return cost;
    }


}
