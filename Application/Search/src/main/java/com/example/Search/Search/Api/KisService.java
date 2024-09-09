package com.example.Search.Search.Api;

import com.example.Search.Common.Entity.TokenInfo;
import com.example.Search.Common.Repository.TokenInfoRepository;
import com.example.Search.Search.SearchDTO.CurrentPriceDTO;
import com.example.Search.Search.SearchDTO.DailyStockPriceDTO;
import com.example.Search.Search.SearchDTO.SearchResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KisService {

    private final GeneratedToken generatedToken;
    private final TokenInfoRepository tokenInfoRepository;

    public static final String REST_BASE_URL = "https://openapi.koreainvestment.com:9443";

    WebClient webClient = WebClient.create(REST_BASE_URL);

    @Value("${app.key}")
    private String apiKey;

    @Value("${app.secret}")
    private String apiSecret;

    @Value("${app.fourth-key}")
    private String FOURTH_API_KEY;

    @Value("${app.fourth-secret}")
    private String FOURTH_API_SECRET;

    public SearchResponseDto getCurrentPrice(String stockCode) {
        try {
            CurrentPriceDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .build())
                    .header("authorization", "Bearer " + generatedToken.getAccessToken(1L))
                    .header("appkey", apiKey)
                    .header("appsecret", apiSecret)
                    .header("tr_id", "FHKST01010100")
                    .retrieve()
                    .bodyToMono(CurrentPriceDTO.class)
                    .block();

            if (response != null && response.getOutput() != null) {
                return SearchResponseDto.builder()
                        .previousPrice(response.getOutput().getPrdy_vrss())
                        .currentPrice(Long.parseLong(response.getOutput().getStck_prpr()))
                        .previousRate(response.getOutput().getPrdy_ctrt())
                        .build();
            } else {
                log.error("API response is null or doesn't contain expected data for stock code: {}", stockCode);
                return null;
            }
        } catch (Exception e) {
            log.error("Error getting stock price for code {}: {}", stockCode, e.getMessage());
            return null;
        }
    }

    public List<DailyStockPriceDTO> getStockChartList(String stockCode, String period) {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate;
            String type = "D";

            switch (period) {
                case "1W":
                    startDate = endDate.minusWeeks(1);
                    break;
                case "1M":
                    startDate = endDate.minusMonths(1);
                    break;
                case "3M":
                    startDate = endDate.minusMonths(3);
                    break;
                case "1Y":
                    startDate = endDate.minusYears(1);
                    type = "W";
                    break;
                default:
                    startDate = endDate.minusMonths(1);  // 기본값은 1달
            }

            final String PERIOD = type;

            ResponseEntity<String> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-daily-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .queryParam("FID_INPUT_DATE_1", startDate.format(DateTimeFormatter.BASIC_ISO_DATE))
                            .queryParam("FID_INPUT_DATE_2", endDate.format(DateTimeFormatter.BASIC_ISO_DATE))
                            .queryParam("FID_PERIOD_DIV_CODE", PERIOD)
                            .queryParam("FID_ORG_ADJ_PRC", "0")
                            .build())
                    .header("content-type", "application/json; charset=utf-8")
                    .header("authorization", "Bearer " + generatedToken.getAccessToken(4L))
                    .header("appkey", FOURTH_API_KEY)
                    .header("appsecret", FOURTH_API_SECRET)
                    .header("tr_id", "FHKST03010100")
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode output2Node = rootNode.get("output2");

                if (output2Node != null && output2Node.isArray()) {
                    List<DailyStockPriceDTO> priceList = new ArrayList<>();
                    for (JsonNode dailyPrice : output2Node) {
                        String dateStr = dailyPrice.get("stck_bsop_date").asText();
                        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
                        long closingPrice = Long.parseLong(dailyPrice.get("stck_clpr").asText());
                        priceList.add(new DailyStockPriceDTO(date, closingPrice));
                    }
                    return priceList;
                } else {
                    log.error("API response doesn't contain expected data structure for stock code: {}", stockCode);
                    return new ArrayList<>();
                }
            } else {
                log.error("API request failed for stock code: {}", stockCode);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error getting stock price list for code {}: {}", stockCode, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Scheduled(cron = "0 0 1 * * * ")
    private void updateAccessToken() {
        TokenInfo fourthTokenInfo = tokenInfoRepository.findById(4L).orElse(null);

        if (fourthTokenInfo != null) {
            String newFourthToken = generatedToken.generateFourthAccessToken();
            fourthTokenInfo.setAccessToken(newFourthToken);
            tokenInfoRepository.save(fourthTokenInfo);
        }
    }

}
