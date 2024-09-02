package com.example.Mypage.Mypage.Kafka.ouput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, Object message) {

        try {
            ProducerRecord<String, Object> record = new ProducerRecord<>(topic, message);
            System.out.println(kafkaTemplate.send(record).get());
//            log.info("Auth로 메시지 전송완료. 토픽 : {}, 파티션 : {} , 오프셋: {}", metadata.topic(), metadata.partition(),
//                    metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
