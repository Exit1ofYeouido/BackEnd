package com.example.Reward.Advertisement.Kafka.Service;

import com.example.Reward.Common.Kafka.GiveStockProduceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaOutputService {
    private final KafkaTemplate<String,Object> kafkaTemplate;


    public void giveStock(Long memId, double stockAmount, String code, String enterpriseName, int cost) {

        kafkaTemplate.send("give-stock", GiveStockProduceDto.builder()
                .memId(memId)
                .price(cost)
                .amount(stockAmount)
                .enterpriseName(enterpriseName)
                .code(code).build());
    }


}
