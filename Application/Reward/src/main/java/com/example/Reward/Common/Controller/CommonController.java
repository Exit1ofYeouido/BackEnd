package com.example.Reward.Common.Controller;

import com.example.Reward.Common.Dto.DetailEnterPriseResponseDto;
import com.example.Reward.Common.Service.DetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="리워드 공통 API")
@RequiredArgsConstructor
public class CommonController {

    private final DetailService detailService;

    @GetMapping("/detail")
    @Operation(description = "기업 세부정보 소개")
    public ResponseEntity<DetailEnterPriseResponseDto> detailEnterprise(@RequestParam("enterpriseName") String enterpriseName){
        DetailEnterPriseResponseDto detailEnterPriseResponseDto=detailService.getEnterpriseDetail(enterpriseName);
        return ResponseEntity.ok(detailEnterPriseResponseDto);
    }
}
