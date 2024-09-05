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
            log.info("회원가입 Auth 요청 => {}", kafkaTemplate.send(record).get());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
