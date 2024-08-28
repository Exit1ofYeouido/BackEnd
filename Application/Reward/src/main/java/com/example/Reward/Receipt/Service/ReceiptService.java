package com.example.Reward.Receipt.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.Reward.Receipt.Dto.out.CheckReceiptResponseDTO;
import com.example.Reward.Receipt.Dto.out.OCRResponseDTO;
import com.example.Reward.Receipt.Dto.out.GetEnterpriseResponseDTO;
import com.example.Reward.Receipt.Entity.Event;
import com.example.Reward.Receipt.Repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
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
    private static final String BASE_URL = "https://1l8mnx9ap5.apigw.ntruss.com";

    public GetEnterpriseResponseDTO getEnterpriseList() {
        List<String> enterpriseList = new ArrayList<>();
        List<Event> eventEnterprises = eventRepository.findEventIdAndEnterpriseNameByRewardAmount();
        for(Event event : eventEnterprises) {
            enterpriseList.add(event.getEnterpriseName());
        }

        return GetEnterpriseResponseDTO
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

    public CheckReceiptResponseDTO analyzeReceipt(String receiptData, String extension) {
        String url = BASE_URL + "/custom/v1/33600/7421306ff3c576bde6b6088961ce77f253b4467347f9348761bde666036c3538/document/receipt";
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

        return CheckReceiptResponseDTO.builder()
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
}
