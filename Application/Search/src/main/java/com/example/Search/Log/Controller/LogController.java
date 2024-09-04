package com.example.Search.Log.Controller;

import com.example.Search.Log.Dto.out.GetHistoryStockResponseDto;
import com.example.Search.Log.Service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="Log API")
@RequestMapping("admin")
public class LogController {

    private final LogService logService;

    @GetMapping("search-history/stock")
    @Operation(description = "주식 종목별 검색량 조회")
    public ResponseEntity<?> gethistoryStock(@RequestParam("code") String code,@RequestParam("year") Integer year
            ,@RequestParam("month") Integer month){

        List<GetHistoryStockResponseDto> getHistoryStockResponseDto =logService.gethistoryStock();

        return ResponseEntity.ok(getHistoryStockResponseDto);
    }


}
