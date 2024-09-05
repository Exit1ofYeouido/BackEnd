package com.example.Mypage.Mypage.Webclient.config;

import com.example.Mypage.Mypage.Webclient.handler.StockPriceSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${approval.key}")
    private String approvalKey;

    @Bean
    public StockPriceSocketHandler stockPriceSocketHandler() {
        return new StockPriceSocketHandler(approvalKey, "005930");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("register websocket handler");
        registry.addHandler(stockPriceSocketHandler(), "/tryitout/H0STCNT0");
    }
}
