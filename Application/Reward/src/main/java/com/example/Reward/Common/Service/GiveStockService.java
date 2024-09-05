package com.example.Reward.Common.Service;

import com.example.Reward.Advertisement.Webclient.Service.GeneratedToken;

import com.example.Reward.Common.Kafka.GiveStockDto;
import com.example.Reward.Common.Repository.EventRepository;
import com.example.Reward.Receipt.Dto.webClient.PresentPriceDTO;
import com.example.Reward.Receipt.Exception.ReceiptExceptions.KISApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GiveStockService {
    private final EventRepository eventRepository;
    private final WebClient webClient;
    @Value("${app.key}")
    private String appKey;
    @Value("${app.secretkey}")
    private String appSecret;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final GeneratedToken generatedToken;
    private static final String STOCK_BASE_URL = "https://openapi.koreainvestment.com:9443";

    public Integer getPrice(String enterpriseName, Long contentId) {
        try {
            String stockCode = eventRepository.findByEnterpriseNameContainingAndContentId(enterpriseName, contentId).getStockCode();
            Mono<PresentPriceDTO> response = webClient.get()
                    .uri(STOCK_BASE_URL + "/uapi/domestic-stock/v1/quotations/inquire-price?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD={param}", stockCode)
                    .header("authorization", "Bearer " + generatedToken.getAccessToken())
                    .header("appkey", appKey)
                    .header("appsecret", appSecret)
                    .header("tr_id", "FHKST01010100")
                    .retrieve()
                    .bodyToMono(PresentPriceDTO.class);
            PresentPriceDTO result = response.block();
            return result.getOutput().getStck_prpr();
        } catch (Exception e) {
            throw new KISApiException();
        }
    }

    public Double calDecimalStock(Integer priceOfStock) {
        BigDecimal price = new BigDecimal(Integer.toString(priceOfStock));
        return BigDecimal.valueOf(100).divide(price, 6, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public void giveStock(Long memberId, String enterpriseName, Long contentId, Integer priceOfStock, Double amountOfStock) {
        String stockCode = eventRepository.findByEnterpriseNameContainingAndContentId(enterpriseName, contentId).getStockCode();
        GiveStockDto giveStockProduceDTO = GiveStockDto.builder()
                .memId(memberId)
                .enterpriseName(enterpriseName)
                .code(stockCode)
                .price(priceOfStock)
                .amount(amountOfStock)
                .build();

        kafkaTemplate.send("test-mo", giveStockProduceDTO);
    }

    public void giveAdStock(Long memId, double stockAmount, String code, String enterpriseName, int cost) {

        kafkaTemplate.send("give-stock", GiveStockDto.builder()
                .memId(memId)
                .price(cost)
                .amount(stockAmount)
                .enterpriseName(enterpriseName)
                .code(code).build());
    }
}
