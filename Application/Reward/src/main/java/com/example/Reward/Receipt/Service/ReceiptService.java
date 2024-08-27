package com.example.Reward.Receipt.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.Reward.Receipt.Dto.out.GetEnterpriseResponseDTO;
import com.example.Reward.Receipt.Entity.Event;
import com.example.Reward.Receipt.Repository.EventRepository;
import com.example.Reward.Receipt.Repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final EventRepository eventRepository;
    private final PopupRepository popupRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public GetEnterpriseResponseDTO findEnterprises() {
        String popupType = "영수증";
        Long memberId = 1L;
        Long checked = popupRepository.exists(popupType, memberId);
        Boolean popupChecked = checked > 0;

        List<String> enterpriseList = new ArrayList<>();
        List<Event> eventEnterprises = eventRepository.findEventIdAndEnterpriseNameByRewardAmount();
        for(Event event : eventEnterprises) {
            enterpriseList.add(event.getEnterpriseName());
        }

        return GetEnterpriseResponseDTO
                .builder()
                .popupChecked(popupChecked)
                .enterprises(enterpriseList)
                .build();
    }

    public String uploadReceiptToS3(MultipartFile receiptImg) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(receiptImg.getSize());
        InputStream inputStream = receiptImg.getInputStream();
        String fileKey = receiptImg.getOriginalFilename();
        amazonS3.putObject(bucket, fileKey, inputStream, metadata);
        return fileKey;
    }

    public String convertImage(MultipartFile receiptImg) throws IOException {
        byte[] fileBytes = receiptImg.getBytes();
        return Base64.getEncoder().encodeToString(fileBytes);
    }

}
