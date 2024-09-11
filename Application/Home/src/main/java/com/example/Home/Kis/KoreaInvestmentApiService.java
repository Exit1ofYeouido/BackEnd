package com.example.Home.Kis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KoreaInvestmentApiService {

    private final GeneratedToken generatedToken;

    @Value("${app.key}")
    private String apiKey;

    @Value("${app.secret}")
    private String apiSecret;

    @Value("${app.fourth-key}")
    private String FOURTH_API_KEY;

    @Value("${app.fourth-secret}")
    private String FOURTH_API_SECRET;


    WebClient webClient = WebClient.create("https://openapi.koreainvestment.com:9443");

    public Long getCurrentAPrice(String stockCode) {
        try {
            ApiResponseDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-time-itemconclusion")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .queryParam("FID_INPUT_HOUR_1", "160000")
                            .build())
                    .header("authorization", "Bearer " + generatedToken.getAccessToken(6L))
                    .header("appkey", apiKey)
                    .header("appsecret", apiSecret)
                    .header("tr_id", "FHPST01060000")
                    .retrieve()
                    .bodyToMono(ApiResponseDTO.class)
                    .block();

            if (response != null && response.getOutput1() != null) {
                return Long.parseLong(response.getOutput1().getStck_prpr());
            } else {
                log.error("API response is null or doesn't contain expected data for stock code: {}", stockCode);
                return 0L;
            }
        } catch (Exception e) {
            log.error("Error getting stock price for code {}: {}", stockCode, e.getMessage());
            return 0L;
        }
    }

    public Long getCurrentBPrice(String stockCode) {
        try {
            ApiResponseDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-time-itemconclusion")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .queryParam("FID_INPUT_HOUR_1", "160000")
                            .build())
                    .header("authorization", "Bearer " + generatedToken.getAccessToken(4L))
                    .header("appkey", FOURTH_API_KEY)
                    .header("appsecret", FOURTH_API_SECRET)
                    .header("tr_id", "FHPST01060000")
                    .retrieve()
                    .bodyToMono(ApiResponseDTO.class)
                    .block();

            if (response != null && response.getOutput1() != null) {
                return Long.parseLong(response.getOutput1().getStck_prpr());
            } else {
                log.error("API response is null or doesn't contain expected data for stock code: {}", stockCode);
                return 0L; // 또는 다른 기본값
            }
        } catch (Exception e) {
            log.error("Error getting stock price for code {}: {}", stockCode, e.getMessage());
            return 0L; // 또는 다른 기본값
        }
    }
}