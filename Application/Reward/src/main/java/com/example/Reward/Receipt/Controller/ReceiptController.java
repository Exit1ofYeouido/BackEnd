package com.example.Reward.Receipt.Controller;

import com.example.Reward.Receipt.Dto.out.CheckReceiptResponseDTO;
import com.example.Reward.Receipt.Dto.out.GetEnterpriseResponseDTO;
import com.example.Reward.Receipt.Service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/receipt")
@Tag(name="영수증 API")
public class ReceiptController {
    private ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/enterprise")
    @Operation(description = "영수증 리워드를 받을 수 있는 기업들의 목록")
    public ResponseEntity<GetEnterpriseResponseDTO> getEnterprise() {
        GetEnterpriseResponseDTO getEnterpriseResponseDTO = receiptService.getEnterpriseList();
        return ResponseEntity.ok(getEnterpriseResponseDTO);
    }

    @PostMapping("check")
    @Operation(description = "영수증 업로드, 해당 가게가 상장돼있는지 확인")
    public ResponseEntity<CheckReceiptResponseDTO> checkReceipt(@RequestPart("receiptImg") MultipartFile receiptImg) throws IOException {
        String extension = receiptService.getExtension(receiptImg);
        String receiptURL = receiptService.uploadReceiptToS3(receiptImg);
        String receiptData = receiptService.convertImage(receiptImg);
        CheckReceiptResponseDTO checkReceiptResponseDTO = receiptService.analyzeReceipt(receiptData, extension);
        GetEnterpriseResponseDTO getEnterpriseResponseDTO = receiptService.getEnterpriseList();
        String checkedEnterpriseName = receiptService.checkEnterpriseName(getEnterpriseResponseDTO.getEnterprises(), checkReceiptResponseDTO.getStoreName());
        if(checkedEnterpriseName.equals("")) {
            checkReceiptResponseDTO.setFind(false);
            System.out.println("hi");
        } else {
            checkReceiptResponseDTO.setFind(true);
            checkReceiptResponseDTO.setEnterpriseName(checkedEnterpriseName);
        }
        checkReceiptResponseDTO.setImgURL(receiptURL);
        return ResponseEntity.ok(checkReceiptResponseDTO);
    }
}
