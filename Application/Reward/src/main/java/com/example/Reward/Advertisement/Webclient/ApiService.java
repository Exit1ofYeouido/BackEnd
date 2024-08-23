package com.example.Reward.Advertisement.Webclient;

import com.example.Reward.Common.Repository.TokenInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final GeneratedToken generatedToken;
    public static final String REST_BASE_URL = "https://openapi.koreainvestment.com:9443";

    WebClient client=WebClient.create();

    @Value("${app.key}")
    public String APPKEY ;

    @Value("${app.secretkey}")
    public String APPSECRET ;
    public double getPrise(String code) {


        String url=REST_BASE_URL+"/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=" +
                "J&fid_input_iscd=" + code;
        Mono<ResponseDto> response=client.get()
                    .uri(url)
                    .header("content-type","application/json")
                    .header("authorization","Bearer " + generatedToken.getAccessToken())
                    .header("appkey",APPKEY)
                    .header("appsecret",APPSECRET)
                    .header("tr_id","FHKST01010100")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ResponseDto.class);

            ResponseDto result=response.block();

        int cost= Integer.parseInt(result.getOutput().getStck_prpr());
        double resultCost=Math.round(((150/cost)*1000000)/1000000);

        return resultCost;
    }


}
