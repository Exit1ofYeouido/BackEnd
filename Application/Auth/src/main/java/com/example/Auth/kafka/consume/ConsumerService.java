package com.example.Auth.kafka.consume;

import com.example.Auth.entity.MemberAuth;
import com.example.Auth.repository.AuthRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final ObjectMapper objectMapper;
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @KafkaListener(topics = "test-auth", groupId = "Auth", concurrency = "3")
    public void consume(@Payload ConsumerRecord<String, String> message) {
        try {
            log.info("Consume 발생: {}", message.value());
            AuthConsumeDTO authConsumeDTO = objectMapper.readValue(message.value(), AuthConsumeDTO.class);
            authConsumeDTO.setMemberPassword(bCryptPasswordEncoder.encode(authConsumeDTO.getMemberPassword()));
            MemberAuth newMemberAuth = authConsumeDTO.toEntity();

            authRepository.save(newMemberAuth);

        } catch (Exception e) {
            log.error("유효하지 않은 데이터 수신: {}, 에러: {}", message.value(), e.getMessage());
        }
    }
}
