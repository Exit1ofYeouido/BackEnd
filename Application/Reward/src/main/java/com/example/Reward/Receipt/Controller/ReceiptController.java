package com.example.Reward.Receipt.Controller;

import com.example.Reward.Common.Service.GiveStockService;
import com.example.Reward.Receipt.Dto.in.RewardRequestDTO;
import com.example.Reward.Receipt.Dto.out.AnalyzeReceiptDTO;
import com.example.Reward.Receipt.Dto.out.CheckReceiptResponseDTO;
import com.example.Reward.Receipt.Dto.out.GetEnterpriseListDTO;
import com.example.Reward.Receipt.Dto.out.RewardResponseDTO;
import com.example.Reward.Receipt.Service.OcrService;
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
    private final ReceiptService receiptService;
    private final GiveStockService giveStockService;
    private final OcrService ocrService;

    public ReceiptController(ReceiptService receiptService, GiveStockService giveStockService, OcrService ocrService) {
        this.receiptService = receiptService;
        this.giveStockService = giveStockService;
        this.ocrService = ocrService;
    }

    @GetMapping("/enterprise")
    @Operation(description = "영수증 리워드를 받을 수 있는 기업들의 목록")
    public ResponseEntity<GetEnterpriseListDTO> getEnterprise() {
        GetEnterpriseListDTO getEnterpriseListDTO = receiptService.getEnterpriseList();
        return ResponseEntity.ok(getEnterpriseListDTO);
    }

    @PostMapping("/check")
    @Operation(description = "영수증 OCR 요청 후 해당 가게가 상장돼 있는지 확인")
    public ResponseEntity<CheckReceiptResponseDTO> checkReceipt(@RequestPart("receiptImg") MultipartFile receiptImg) throws IOException {
        String extension = receiptService.getExtension(receiptImg);
        String receiptURL = receiptService.uploadReceiptToS3(receiptImg);
        String receiptData = receiptService.convertImage(receiptImg);
        AnalyzeReceiptDTO analyzeReceiptDTO = ocrService.getReceiptInfo(receiptURL, receiptData, extension);
        GetEnterpriseListDTO getEnterpriseListDTO = receiptService.getEnterpriseList();
        String checkedEnterpriseName = receiptService.checkEnterpriseName(getEnterpriseListDTO.getEnterprises(), analyzeReceiptDTO.getStoreName(), receiptURL);
        CheckReceiptResponseDTO checkReceiptResponseDTO = CheckReceiptResponseDTO.builder()
                .find(checkedEnterpriseName.isEmpty() ? false:true)
                .storeName(analyzeReceiptDTO.getStoreName())
                .price(analyzeReceiptDTO.getPrice())
                .dealTime(analyzeReceiptDTO.getDealTime())
                .approvalNum(analyzeReceiptDTO.getApprovalNum())
                .imgURL(receiptURL)
                .enterpriseName(checkedEnterpriseName)
                .build();
        return ResponseEntity.ok(checkReceiptResponseDTO);
    }

    @PostMapping("/")
    @Operation(description = "사용자 확인 후 영수증 정보 저장 및 리워드 제공")
    public ResponseEntity<RewardResponseDTO> rewardStock(@RequestHeader("memberId") String memberId, @RequestBody RewardRequestDTO rewardRequestDTO) {
        Integer priceOfStock = giveStockService.getPrice(rewardRequestDTO.getEnterpriseName(), 2L);
        Double amountOfStock = giveStockService.calDecimalStock(priceOfStock);
        RewardResponseDTO rewardResponseDTO = receiptService.giveStockAndSaveReceipt(Long.valueOf(memberId), rewardRequestDTO, priceOfStock, amountOfStock);
        return ResponseEntity.ok(rewardResponseDTO);
    }
}
