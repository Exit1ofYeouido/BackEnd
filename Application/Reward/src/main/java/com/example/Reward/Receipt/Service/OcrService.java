package com.example.Reward.Receipt.Service;

import com.example.Reward.Receipt.Dto.out.AnalyzeReceiptDTO;
import com.example.Reward.Receipt.Dto.webClient.OCRResponseDTO;
import com.example.Reward.Receipt.Dto.webClient.StarbucksResponseDTO;
import com.example.Reward.Receipt.Exception.ReceiptExceptions.MissingOcrInfoException;
import com.example.Reward.Receipt.Exception.ReceiptExceptions.OcrErrorException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OcrService {

    private final WebClient webClient;
    @Value("${x-ocr-secret}")
    private String ocrSecret;
    @Value("${x-ocr-secret-starbucks}")
    private String ocrSecretStarbucks;
    private StarbucksResponseDTO starbucksResponseDTO;

    @Transactional
    public AnalyzeReceiptDTO getReceiptInfo(String receiptURL, String receiptData, String  extension) {
        List<Map<String, String>> imageDataList = new ArrayList<>();
        Map<String, String> imageData = new HashMap<>();
        imageData.put("format", extension);
        imageData.put("data", receiptData);
        imageData.put("name", "receipt");

        imageDataList.add(imageData);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("images", imageDataList);
        requestBody.put("lang", "ko");
        requestBody.put("requestId", "string");
        requestBody.put("timestamp", System.currentTimeMillis());
        requestBody.put("version", "V2");

        OCRResponseDTO ocrResult = requestOCR(requestBody);;
        AnalyzeReceiptDTO analyzeReceiptDTO = analyzeResult(ocrResult, receiptURL);
        if(analyzeReceiptDTO.getApprovalNum() == null) {
            StarbucksResponseDTO starbucksResult = requestStarbucks(requestBody);
            AnalyzeReceiptDTO starbucksDTO = analyzeStarbucks(starbucksResult, receiptURL, analyzeReceiptDTO);
            return starbucksDTO;
        }
        return analyzeReceiptDTO;
    }

    private OCRResponseDTO requestOCR(Map<String, Object> requestBody) {
        Mono<OCRResponseDTO> response = webClient.post()
                .uri("https://1l8mnx9ap5.apigw.ntruss.com/custom/v1/33600/7421306ff3c576bde6b6088961ce77f253b4467347f9348761bde666036c3538/document/receipt")
                .header("X-OCR-SECRET", ocrSecret)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OCRResponseDTO.class);

        OCRResponseDTO ocrResponseDTO = response.block();

        return ocrResponseDTO;
    }

    private AnalyzeReceiptDTO analyzeResult(OCRResponseDTO ocrResponseDTO, String receiptURL) {
        String storeName = "";
        String price = "";
        String dealTime = "";
        String approvalNum = "";
        ArrayList<String> missingReceiptInfo = new ArrayList<>();
        try {
            try {
                storeName = ocrResponseDTO.getImages()[0].getReceipt().getResult().getStoreInfo().getName().getText();
            } catch (Exception e) {
                missingReceiptInfo.add("storeName");
            }
            try {
                price = ocrResponseDTO.getImages()[0].getReceipt().getResult().getTotalPrice().getPrice().getText();
            } catch (Exception e) {
                missingReceiptInfo.add("price");
            }
            try {
                String date = ocrResponseDTO.getImages()[0].getReceipt().getResult().getPaymentInfo().getDate().getText();
                String time = ocrResponseDTO.getImages()[0].getReceipt().getResult().getPaymentInfo().getTime().getText();
                StringBuilder dealTimeBuilder = new StringBuilder();
                dealTimeBuilder.append(date)
                        .append(" ")
                        .append(time);
                dealTime = dealTimeBuilder.toString();
            } catch (Exception e) {
                missingReceiptInfo.add("dealTime");
            }
            try {
                approvalNum = ocrResponseDTO.getImages()[0].getReceipt().getResult().getPaymentInfo().getConfirmNum().getText();
            } catch (Exception e) {
                missingReceiptInfo.add("approvalNum");
            }
            if (missingReceiptInfo.size() != 0) {
                throw new MissingOcrInfoException(receiptURL, missingReceiptInfo);
            }
            return AnalyzeReceiptDTO.builder()
                    .storeName(storeName)
                    .price(price)
                    .dealTime(dealTime)
                    .approvalNum(approvalNum)
                    .build();
        } catch (MissingOcrInfoException e) {
            if(storeName.equals("STARBUCKS") && missingReceiptInfo.size()==1) {
                return AnalyzeReceiptDTO.builder()
                        .storeName(storeName)
                        .price(price)
                        .dealTime(dealTime)
                        .build();
            }
            throw e;
        } catch (OcrErrorException e) {
            throw new OcrErrorException(receiptURL);
        }
    }

    private StarbucksResponseDTO requestStarbucks(Map<String, Object> requestBody) {
        Mono<StarbucksResponseDTO> response = webClient.post()
                .uri("https://ctg8h35fzf.apigw.ntruss.com/custom/v1/34154/4aa6969a388a81e1c82e2acd27a159eb6d8bcacde795ea94da661f213bf2429c/infer")
                .header("X-OCR-SECRET", ocrSecretStarbucks)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(StarbucksResponseDTO.class);

        StarbucksResponseDTO starbucksResponseDTO = response.block();

        return starbucksResponseDTO;
    }

    private AnalyzeReceiptDTO analyzeStarbucks(StarbucksResponseDTO starbucksResult, String receiptURL, AnalyzeReceiptDTO analyzeReceiptDTO) {
        try {
            String approvalNum = starbucksResult.getImages()[0].getFields()[0].getInferText();
            String storeName = analyzeReceiptDTO.getStoreName();
            String price = analyzeReceiptDTO.getPrice();
            String dealTime = analyzeReceiptDTO.getDealTime();
            return AnalyzeReceiptDTO.builder()
                    .storeName(storeName)
                    .price(price)
                    .dealTime(dealTime)
                    .approvalNum(approvalNum)
                    .build();
        } catch (MissingOcrInfoException e) {
            ArrayList<String> missingReceiptInfo = new ArrayList<>();
            missingReceiptInfo.add("approvalNum");
            throw new MissingOcrInfoException(receiptURL, missingReceiptInfo);
        } catch (OcrErrorException e) {
            throw new OcrErrorException(receiptURL);
        }
    }
}
