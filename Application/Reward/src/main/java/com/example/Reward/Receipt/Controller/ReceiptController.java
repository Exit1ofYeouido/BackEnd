package com.example.Reward.Receipt.Controller;

import com.example.Reward.Receipt.Dto.out.GetEnterpriseResponseDTO;
import com.example.Reward.Receipt.Service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipt")
@Tag(name="영수증 API")
public class ReceiptController {
    private ReceiptService receiptService;

//    public ReceiptController receiptController(ReceiptService receiptService) {
//        this.receiptService = receiptService;
//    }

    @GetMapping("/enterprise")
    @Operation(description = "영수증 리워드를 받을 수 있는 기업들의 목록")
    public ResponseEntity<GetEnterpriseResponseDTO> getEnterprise() {
        GetEnterpriseResponseDTO getEnterpriseResponseDTO = receiptService.findEnterprises();
        return ResponseEntity.ok(getEnterpriseResponseDTO);
    }

}
