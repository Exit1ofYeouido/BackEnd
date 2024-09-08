package com.example.Reward.Receipt.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.Reward.Common.Entity.Event;
import com.example.Reward.Common.Repository.EventRepository;
import com.example.Reward.Common.Service.GiveStockService;

import com.example.Reward.Receipt.Dto.in.RewardRequestDTO;
import com.example.Reward.Receipt.Dto.out.*;
import com.example.Reward.Receipt.Entity.ReceiptLog;
import com.example.Reward.Receipt.Entity.ReceiptLogKey;
import com.example.Reward.Receipt.Exception.ReceiptExceptions.*;
import com.example.Reward.Receipt.Repository.ReceiptLogRepository;
import com.example.Reward.Receipt.Util.GetLongestCommonSubstring;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final GiveStockService giveStockService;
    private final EventRepository eventRepository;
    private final ReceiptLogRepository receiptLogRepository;
    private final AmazonS3 amazonS3;
    private final WebClient webClient;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${x-ocr-secret}")
    private String ocrSecret;
    private static final String OCR_BASE_URL = "https://1l8mnx9ap5.apigw.ntruss.com";
    private final GetLongestCommonSubstring getLongestCommonSubstring;

    @Transactional
    public GetEnterpriseListDTO getEnterpriseList() throws NoStockException {
        List<String> enterpriseList = new ArrayList<>();
        List<Event> eventEnterprises = eventRepository.findByRewardAmountGreaterThanEqualAndContentId(1L,2L);
        if (eventEnterprises.isEmpty()) {
            throw new NoStockException();
        }
        for(Event event : eventEnterprises) {
            enterpriseList.add(event.getEnterpriseName());
        }
        return GetEnterpriseListDTO
                .builder()
                .enterprises(enterpriseList)
                .build();
    }
    @Transactional
    public String getExtension(MultipartFile receiptImg) {
        String originalFileName = receiptImg.getOriginalFilename();
        if(originalFileName != null && originalFileName.contains(".")) {
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
            return switch (extension) {
                case "jpg", "jpeg" -> "jpg";
                case "png" -> "png";
                case "svg" -> "svg";
                default -> throw new InvalidFileExtensionException(extension);
            };
        }
        else  {
            String contentType = receiptImg.getContentType();
            if(contentType != null) {
                return switch (contentType) {
                    case "image/jpeg", "image/jpg" -> "jpg";
                    case "image/png" -> "png";
                    case "image/svg+xml" -> "svg";
                    default -> throw new InvalidFileExtensionException(contentType);
                };
            }
            else throw new InvalidFileExtensionException("unknown");
        }
    }

    @Transactional
    public String uploadReceiptToS3(MultipartFile receiptImg) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(receiptImg.getSize());
            metadata.setContentType(receiptImg.getContentType());
            InputStream inputStream = receiptImg.getInputStream();
            String fileKey = "receipts/" + receiptImg.getOriginalFilename();
            amazonS3.putObject(bucket, fileKey, inputStream, metadata);
            return fileKey;
        } catch (Exception e) {
            throw new S3UploadFailedException();
        }
    }

    @Transactional
    public String convertImage(MultipartFile receiptImg) throws IOException {
        byte[] fileBytes = receiptImg.getBytes();
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    @Transactional
    public AnalyzeReceiptDTO analyzeReceipt(String receiptURL, String receiptData, String extension) {
        try {
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

            String storeName = "";
            String price = "";
            String dealTime = "";
            String approvalNum = "";
            ArrayList<String> missingReceiptInfo = new ArrayList<>();
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
            if(missingReceiptInfo.size()!=0) {
                throw new MissingOcrInfoException(receiptURL, missingReceiptInfo);
            }
            return AnalyzeReceiptDTO.builder()
                    .storeName(storeName)
                    .price(price)
                    .dealTime(dealTime)
                    .approvalNum(approvalNum)
                    .build();
        } catch (MissingOcrInfoException e) {
            throw e;
        } catch (OcrErrorException e) {
            throw new OcrErrorException(receiptURL);
        }
    }

    @Transactional
    public String checkEnterpriseName(List<String> enterprises, String storeName, String receiptURL) {
        String longest = "";
        if(storeName.equals("STARBUCKS")) return "이마트";
        for(String name : enterprises) {
            String detected = getLongestCommonSubstring.getlongestCommonSubstring(name, storeName);
            if(detected.length()>longest.length() && detected.length()>name.length()/2) {
                longest = detected;
            }
        }
        if(!longest.isEmpty()) {
            return eventRepository.findByEnterpriseNameContaining(longest).getEnterpriseName();
        }
        throw new StockNotFoundException(storeName, receiptURL);
    }

    @Transactional
    public RewardResponseDTO giveStockAndSaveReceipt(Long memberId, RewardRequestDTO rewardRequestDTO, Integer priceOfStock, Double amountOfStock) {
        try {
            int existReceiptLogCount = receiptLogRepository.countByApprovalNumberAndDealTime(rewardRequestDTO.getApprovalNum(), rewardRequestDTO.getDealTime());
            if(existReceiptLogCount > 0) throw new ExistingReceiptException(rewardRequestDTO.getImgURL());
            giveStockService.giveStock(memberId, rewardRequestDTO.getEnterpriseName(), 2L, priceOfStock, amountOfStock);
            Event event = eventRepository.findByEnterpriseNameContainingAndContentId(rewardRequestDTO.getEnterpriseName(), 2L);
            event.setRewardAmount(event.getRewardAmount() - amountOfStock);
            eventRepository.save(event);

            ReceiptLog receiptLog = ReceiptLog.builder()
                    .receiptLogKey(ReceiptLogKey.builder()
                            .approvalNumber(rewardRequestDTO.getApprovalNum())
                            .dealTime(rewardRequestDTO.getDealTime())
                            .build())
                    .storeName(rewardRequestDTO.getStoreName())
                    .price(rewardRequestDTO.getPrice())
                    .imgPath(rewardRequestDTO.getImgURL())
                    .enterpriseName(rewardRequestDTO.getEnterpriseName())
                    .memberId(memberId)
                    .build();
            receiptLogRepository.save(receiptLog);

            RewardResponseDTO rewardResponseDTO = RewardResponseDTO.builder()
                    .name(rewardRequestDTO.getEnterpriseName())
                    .amount(amountOfStock)
                    .build();
            return rewardResponseDTO;
        } catch (ExistingReceiptException e) {
            throw e;
        } catch (GiveStockErrorException e) {
            throw new GiveStockErrorException();
        }
    }


}
