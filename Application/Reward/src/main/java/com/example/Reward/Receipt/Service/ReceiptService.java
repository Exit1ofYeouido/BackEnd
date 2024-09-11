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
    public String checkEnterpriseName(List<String> enterprises, String storeName, String receiptURL) {
        String longest = "";
        if(storeName.equals("STARBUCKS")) return "이마트";
        for(String name : enterprises) {
            String detected = getLongestCommonSubstring.getlongestCommonSubstring(name, storeName);
            if(detected.length()>longest.length()) {
                if(name.length()<=3) {
                    if(detected.equals(name)) longest = detected;
                }
                else if(name.length()<=5) {
                    if(detected.length()>=3) longest = detected;
                }
                else if(detected.length()>=name.length()/2) longest = detected;
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
            int existReceiptLogCountByPath = receiptLogRepository.countByImgPath(rewardRequestDTO.getImgURL());
            if(existReceiptLogCountByPath > 0) throw new ExistingPathException();
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
                    .stockCode(event.getStockCode())
                    .build();
            return rewardResponseDTO;
        } catch (ExistingReceiptException | ExistingPathException e) {
            throw e;
        } catch (Exception e) {
            throw new GiveStockErrorException();
        }
    }


}
