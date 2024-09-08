package com.example.Mypage.Mypage.Webclient.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
public class StockPriceSocketHandler extends TextWebSocketHandler {
    private WebSocketSession session;
    private ObjectMapper mapper = new ObjectMapper();

    private final String approvalKey;
    private final String trKey;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public StockPriceSocketHandler(String approvalKey, String trKey) {
        this.approvalKey = approvalKey;
        this.trKey = trKey;
    }

    public String getLatestMessage() throws InterruptedException {
        return messageQueue.poll();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String[] stockInfo = message.getPayload().split("\\^");
        if (stockInfo.length >= 30) {
            messageQueue.offer(stockInfo[0]);
        }

        if (stockInfo.length >= 1) {
            messageQueue.offer(stockInfo[0]);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        super.afterConnectionEstablished(session);

        String jsonRequest = createJsonRequest();
        log.info("request : {}", jsonRequest);

        session.sendMessage(new TextMessage(jsonRequest));
    }

    private String createJsonRequest() throws JsonProcessingException {
        Map<String, String> header = createHeader();
        Map<String, Map<String, String>> body = createBody();

        Map<String, Object> request = new HashMap<>();
        request.put("header", header);
        request.put("body", body);

        return mapper.writeValueAsString(request);
    }

    private Map<String, String> createHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("approval_key", approvalKey);
        header.put("custtype", "P");
        header.put("tr_type", "1");
        header.put("content-type", "utf-8");
        return header;
    }

    private Map<String, Map<String, String>> createBody() {
        Map<String, Map<String, String>> body = new HashMap<>();
        Map<String, String> input = new HashMap<>();

        input.put("tr_id", "H0STASP0");
        input.put("tr_key", trKey);
        body.put("input", input);

        return body;
    }
}
