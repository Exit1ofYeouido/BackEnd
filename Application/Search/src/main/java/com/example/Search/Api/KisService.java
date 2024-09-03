package com.example.Search.Api;

import com.example.Search.SearchDTO.CurrentPriceDTO;
import com.example.Search.SearchDTO.DailyStockPriceDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KisService {

    private final WebClient webClient;

    @Value("${korea.investment.api.key}")
    private String apiKey;

    @Value("${korea.investment.api.secret}")
    private String apiSecret;

    @Value("${api.token}")
    private String apiToken;

    public KisService(@Value("https://openapi.koreainvestment.com:9443") String apiUrl) {
        this.webClient = WebClient.create(apiUrl);
    }

    public Long getCurrentPrice(String stockCode) {

        try {
            CurrentPriceDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .build())
                    .header("authorization", "Bearer " + getToken())
                    .header("appkey", "PSf02ZJFWOatPQuYU0f2eEaahY4y5o9aP70q")
                    .header("appsecret", "sG4RxxJZej+oaePWgIA5fozlRR3YBCN2eSNBnW1p4iNjyIldK1HCi3rzMV21zAXf9xOZ0VolAWytiP5QpLemXyApDnaOKbLwR2jbdSsFdg/2SlUxaMoVrZxdjMtonM4IsfEjvWNcZM8ubvK/Wk8VawLdn01fc9gsx2SIRQNjWqctyLiUlf8=")
                    .header("tr_id", "FHKST01010100")
                    .retrieve()
                    .bodyToMono(CurrentPriceDTO.class)
                    .block();

            System.out.println("response ::: " + response.getOutput());
            if (response != null && response.getOutput() != null) {
                return Long.parseLong(response.getOutput().getStck_prpr());
            } else {
                log.error("API response is null or doesn't contain expected data for stock code: {}", stockCode);
                return 0L;
            }
        } catch (Exception e) {
            log.error("Error getting stock price for code {}: {}", stockCode, e.getMessage());
            return 0L;
        }
    }

    public String getPreviousPrice(String stockCode) {
        try {
            CurrentPriceDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .build())
                    .header("authorization", "Bearer " + getToken())
                    .header("appkey", "PSf02ZJFWOatPQuYU0f2eEaahY4y5o9aP70q")
                    .header("appsecret", "sG4RxxJZej+oaePWgIA5fozlRR3YBCN2eSNBnW1p4iNjyIldK1HCi3rzMV21zAXf9xOZ0VolAWytiP5QpLemXyApDnaOKbLwR2jbdSsFdg/2SlUxaMoVrZxdjMtonM4IsfEjvWNcZM8ubvK/Wk8VawLdn01fc9gsx2SIRQNjWqctyLiUlf8=")
                    .header("tr_id", "FHKST01010100")
                    .retrieve()
                    .bodyToMono(CurrentPriceDTO.class)
                    .block();

            System.out.println("response ::: " + response.getOutput());
            if (response != null && response.getOutput() != null) {
                return response.getOutput().getPrdy_vrss();
            } else {
                log.error("API response is null or doesn't contain expected data for stock code: {}", stockCode);
                return "";
            }
        } catch (Exception e) {
            log.error("Error getting stock price for code {}: {}", stockCode, e.getMessage());
            return "";
        }
    }

    public String getPreviousRate(String stockCode) {
        try {
            CurrentPriceDTO response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .build())
                    .header("authorization", "Bearer " + getToken())
                    .header("appkey", "PSf02ZJFWOatPQuYU0f2eEaahY4y5o9aP70q")
                    .header("appsecret", "sG4RxxJZej+oaePWgIA5fozlRR3YBCN2eSNBnW1p4iNjyIldK1HCi3rzMV21zAXf9xOZ0VolAWytiP5QpLemXyApDnaOKbLwR2jbdSsFdg/2SlUxaMoVrZxdjMtonM4IsfEjvWNcZM8ubvK/Wk8VawLdn01fc9gsx2SIRQNjWqctyLiUlf8=")
                    .header("tr_id", "FHKST01010100")
                    .retrieve()
                    .bodyToMono(CurrentPriceDTO.class)
                    .block();

            System.out.println("response ::: " + response.getOutput());
            if (response != null && response.getOutput() != null) {
                return response.getOutput().getPrdy_ctrt();
            } else {
                log.error("API response is null or doesn't contain expected data for stock code: {}", stockCode);
                return "";
            }
        } catch (Exception e) {
            log.error("Error getting stock price for code {}: {}", stockCode, e.getMessage());
            return "";
        }
    }

    public List<DailyStockPriceDTO> getStockPriceList(String stockCode, String period) {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate;

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
                default:
                    startDate = endDate.minusMonths(1);  // 기본값은 1달
            }


            ResponseEntity<String> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/uapi/domestic-stock/v1/quotations/inquire-daily-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .queryParam("FID_INPUT_DATE_1", startDate.format(DateTimeFormatter.BASIC_ISO_DATE))
                            .queryParam("FID_INPUT_DATE_2", endDate.format(DateTimeFormatter.BASIC_ISO_DATE))
                            .queryParam("FID_PERIOD_DIV_CODE", "D")
                            .queryParam("FID_ORG_ADJ_PRC", "0")
                            .build())
                    .header("content-type", "application/json; charset=utf-8")
                    .header("authorization", "Bearer " + getToken())
                    .header("appkey", apiKey)
                    .header("appsecret", apiSecret)
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

    private String getToken() {
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6ImYyZmQ1NzQ4LTRjMDMtNGYyOC1hOGE1LTczYmVkZTg1YjQ2NSIsInByZHRfY2QiOiIiLCJpc3MiOiJ1bm9ndyIsImV4cCI6MTcyNTQxMzE4NiwiaWF0IjoxNzI1MzI2Nzg2LCJqdGkiOiJQU2YwMlpKRldPYXRQUXVZVTBmMmVFYWFoWTR5NW85YVA3MHEifQ.M0z6nw_MpJdOVSbk_CakLbyN32ZHbFVhMd5XtUkVSCtm_i_TmM53WauAfSiP9mx1o11ab9gQMRV4TRV6I4hcJQ";
    }

}
