package com.example.Mypage.Mypage.Kafka.Input;


import com.example.Mypage.Mypage.Kafka.Dto.GiveStockDto;
import com.example.Mypage.Mypage.Service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MypageListener {

    private final MyService myService;

    @KafkaListener(topics="give-stock")
    public void giveStock(GiveStockDto giveStockDto){
        myService.giveStock(giveStockDto);
    }


}
