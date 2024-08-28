package com.example.Reward.Receipt.Controller;

import com.example.Reward.Receipt.Dto.in.RewardRequestDTO;
import com.example.Reward.Receipt.Dto.out.CheckReceiptResponseDTO;
import com.example.Reward.Receipt.Dto.out.GetEnterpriseResponseDTO;
import com.example.Reward.Receipt.Dto.webClient.PresentPriceDTO;
import com.example.Reward.Receipt.Service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

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

    @PostMapping("/check")
    @Operation(description = "영수증 OCR 요청 후 해당 가게가 상장돼 있는지 확인")
    public ResponseEntity<CheckReceiptResponseDTO> checkReceipt(@RequestPart("receiptImg") MultipartFile receiptImg) throws IOException {
        String extension = receiptService.getExtension(receiptImg);
        String receiptURL = receiptService.uploadReceiptToS3(receiptImg);
        String receiptData = receiptService.convertImage(receiptImg);
        CheckReceiptResponseDTO checkReceiptResponseDTO = receiptService.analyzeReceipt(receiptData, extension);
        GetEnterpriseResponseDTO getEnterpriseResponseDTO = receiptService.getEnterpriseList();
        String checkedEnterpriseName = receiptService.checkEnterpriseName(getEnterpriseResponseDTO.getEnterprises(), checkReceiptResponseDTO.getStoreName());
        if(checkedEnterpriseName.equals("")) {
            checkReceiptResponseDTO.setFind(false);
        } else {
            checkReceiptResponseDTO.setFind(true);
            checkReceiptResponseDTO.setEnterpriseName(checkedEnterpriseName);
        }
        checkReceiptResponseDTO.setImgURL(receiptURL);
        return ResponseEntity.ok(checkReceiptResponseDTO);
    }

    @PostMapping("/")
    @Operation(description = "사용자 확인 후 영수증 정보 저장 및 리워드 제공")
    public ResponseEntity<Integer> rewardStock(@RequestBody RewardRequestDTO rewardRequestDTO) {
        Long memberId = 1L;
        Integer priceOfStock = receiptService.getPrice(rewardRequestDTO.getEnterpriseName());
//        double amountOfStock = receiptService.calDecimal(priceOfStock);
//        receiptService.giveStock();
//        receiptService.saveReceipt();
        return ResponseEntity.ok(priceOfStock);

    }

}
