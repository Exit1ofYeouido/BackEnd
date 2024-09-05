package com.example.Search.Common.Kafka;

import com.example.Search.Common.Entity.MemberStockHolding;
import com.example.Search.Common.Entity.Stock;
import com.example.Search.Common.Repository.MemberStockHoldingRepository;
import com.example.Search.Common.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final MemberStockHoldingRepository memberStockHoldingRepository;
    private final StockRepository stockRepository;

    @KafkaListener(topics="give-stock", groupId = "search")
    public void listener(GiveStockDTO giveStockDTO) {
        Stock stock = stockRepository.findByCode(giveStockDTO.getCode());
        Optional<MemberStockHolding> isHolding = memberStockHoldingRepository.findByMemberIdAndStockCode(giveStockDTO.getMemId(), giveStockDTO.getCode());
        if(isHolding.isEmpty()) {
            MemberStockHolding memberStockHolding = new MemberStockHolding(giveStockDTO.getMemId(), stock);
            memberStockHoldingRepository.save(memberStockHolding);
        }
    }
}
