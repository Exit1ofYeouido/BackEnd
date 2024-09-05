package com.example.Mypage.Common.Sms;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageUtil {

    private final DefaultMessageService messageService;
    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    public MessageUtil(@Value("${coolsms.apikey}") String apiKey,
                       @Value("${coolsms.apisecret}") String apiSecretKey) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }


    public boolean sendMessage(String text, String receiveNumber) {
        try {
            Message message = new Message();

            message.setFrom(fromNumber);
            message.setTo(receiveNumber);
            message.setText(text);

            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
            return true;
        } catch (Exception e) {
            log.error("문자 발송 오류발생 {} ", e.getMessage());
            return false;
        }

    }

}
