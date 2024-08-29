package com.example.Reward.Receipt.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.Reward.Receipt.Dto.in.RewardRequestDTO;
import com.example.Reward.Receipt.Dto.out.*;
import com.example.Reward.Receipt.Dto.webClient.PresentPriceDTO;
import com.example.Reward.Receipt.Entity.Event;
import com.example.Reward.Receipt.Repository.EventRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final EventRepository eventRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final WebClient webClient;
    @Value("${x-ocr-secret}")
    private String ocrSecret;
    private static final String OCR_BASE_URL = "https://1l8mnx9ap5.apigw.ntruss.com";
    private static final String STOCK_BASE_URL = "https://openapi.koreainvestment.com:9443";
    @Value("${authorization}")
    private String authorization;
    @Value("${appKey}")
    private String appKey;
    @Value("${appSecret}")
    private String appSecret;
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public GetEnterpriseListDTO getEnterpriseList() {
        List<String> enterpriseList = new ArrayList<>();
        List<Event> eventEnterprises = eventRepository.findEventIdAndEnterpriseNameByRewardAmount();
        for(Event event : eventEnterprises) {
            enterpriseList.add(event.getEnterpriseName());
        }

        return GetEnterpriseListDTO
                .builder()
                .enterprises(enterpriseList)
                .build();
    }

    public String getExtension(MultipartFile receiptImg) {
        String originalFileName = receiptImg.getOriginalFilename();
        if(originalFileName != null && originalFileName.contains(".")) {
            return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }
        else  {
            String contentType = receiptImg.getContentType();
            if(contentType != null) {
                return switch (contentType) {
                    case "image/jpeg", "image/jpg" -> "jpg";
                    case "image/png" -> "png";
                    case "image/svg" -> "svg";
                    default -> null;
                };
            }
            return null;
        }
    }

    public String uploadReceiptToS3(MultipartFile receiptImg) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(receiptImg.getSize());
        metadata.setContentType(receiptImg.getContentType());
        InputStream inputStream = receiptImg.getInputStream();
        String fileKey = receiptImg.getOriginalFilename();
        amazonS3.putObject(bucket, fileKey, inputStream, metadata);
        return fileKey;
    }

    public String convertImage(MultipartFile receiptImg) throws IOException {
        byte[] fileBytes = receiptImg.getBytes();
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    public AnalyzeReceiptDTO analyzeReceipt(String receiptData, String extension) {
        String url = OCR_BASE_URL + "/custom/v1/33600/7421306ff3c576bde6b6088961ce77f253b4467347f9348761bde666036c3538/document/receipt";
        List<Map<String, String>> imageDataList = new ArrayList<>();
        Map<String, String> imageData = new HashMap<>();
        imageData.put("format", extension);
        imageData.put("data", receiptData);
        imageData.put("name", "receipt4");

        imageDataList.add(imageData);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("images", imageDataList);
        requestBody.put("lang", "ko");
        requestBody.put("requestId", "string");
        requestBody.put("timestamp", System.currentTimeMillis());
        requestBody.put("version", "V2");

        Mono<OCRResponseDTO> response = webClient.post()
                .uri(url)
                .header("X-OCR-SECRET", ocrSecret)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OCRResponseDTO.class);

        OCRResponseDTO ocrResponseDTO = response.block();

        String storeName = ocrResponseDTO.getImages()[0].getReceipt().getResult().getStoreInfo().getName().getText();
        String price = ocrResponseDTO.getImages()[0].getReceipt().getResult().getTotalPrice().getPrice().getText();
        String date = ocrResponseDTO.getImages()[0].getReceipt().getResult().getPaymentInfo().getDate().getText();
        String time = ocrResponseDTO.getImages()[0].getReceipt().getResult().getPaymentInfo().getTime().getText();
        StringBuilder dealTimeBuilder = new StringBuilder();
        dealTimeBuilder.append(date)
                .append(" ")
                .append(time);
        String dealTime = dealTimeBuilder.toString();
        String approvalNum = ocrResponseDTO.getImages()[0].getReceipt().getResult().getPaymentInfo().getConfirmNum().getText();

        return AnalyzeReceiptDTO.builder()
                .storeName(storeName)
                .price(price)
                .dealTime(dealTime)
                .approvalNum(approvalNum)
                .build();
    }

    public String checkEnterpriseName(List<String> enterprises, String storeName) {
        for(String name : enterprises) {
            if(storeName.contains(name)) {
                return name;
            }
        }
        return "";
    }


    public Integer getPrice(String enterpriseName) {
        String stockCode = eventRepository.findByEnterpriseName(enterpriseName);
        Mono<PresentPriceDTO> response = webClient.get()
                .uri(STOCK_BASE_URL + "/uapi/domestic-stock/v1/quotations/inquire-price?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD={param}", stockCode)
                .header("authorization", "Bearer " + authorization)
                .header("appkey", appKey)
                .header("appsecret", appSecret)
                .header("tr_id", "FHKST01010100")
                .retrieve()
                .bodyToMono(PresentPriceDTO.class);
        PresentPriceDTO result = response.block();
        return result.getOutput().getStck_prpr();
    }

    public Double calDecimalStock(Integer priceOfStock) {
        BigDecimal price = new BigDecimal(Integer.toString(priceOfStock));
        return BigDecimal.valueOf(100).divide(price, 6, BigDecimal.ROUND_DOWN).doubleValue();
    }

    @Transactional
    public void giveStockAndSaveReceipt(Long memberId, RewardRequestDTO rewardRequestDTO, Integer priceOfStock, Double amountOfStock) {

        giveStock(memberId, rewardRequestDTO, priceOfStock, amountOfStock);
    }

    public void giveStock(Long memberId, RewardRequestDTO rewardRequestDTO, Integer priceOfStock, Double amountOfStock) {
        String stockCode = eventRepository.findByEnterpriseName(rewardRequestDTO.getEnterpriseName());
         GiveStockDTO giveStockDTO = GiveStockDTO.builder()
                .memberId(memberId)
                .enterpriseName(rewardRequestDTO.getEnterpriseName())
                .code(stockCode)
                .price(priceOfStock)
                .amount(amountOfStock)
                .build();

        kafkaTemplate.send("test-mo", giveStockDTO);
    }

}
