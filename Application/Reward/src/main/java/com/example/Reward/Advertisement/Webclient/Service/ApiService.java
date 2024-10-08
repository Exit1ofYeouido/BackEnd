package com.example.Reward.Advertisement.Webclient.Service;

import com.example.Reward.Advertisement.Webclient.Dto.ResponseDto;
import com.example.Reward.Advertisement.Webclient.Dto.ResultDto;
import lombok.RequiredArgsConstructor;
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

    public ResultDto getPrise(String code) {

        String url=REST_BASE_URL+"/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=" +
                "J&fid_input_iscd=" + code;
        Mono<ResponseDto> response=client.get()
                    .uri(url)
                    .header("authorization","Bearer " + generatedToken.getAccessToken())
                    .header("appkey",APPKEY)
                    .header("appsecret",APPSECRET)
                    .header("tr_id","FHKST01010100")
                    .retrieve()
                    .bodyToMono(ResponseDto.class);

            ResponseDto result=response.block();

        int cost= Integer.parseInt(result.getOutput().getStck_prpr());
        double resultCost=Math.round((150.0 /cost) * 1000000) / 1000000.0;

        ResultDto resultDto=ResultDto.builder()
                .cost(cost)
                .amount(resultCost)
                .build();
        return resultDto;
    }


}
