package com.example.Search.Log.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name="Log API")
@RequestMapping("admin")
public class LogController {



    @GetMapping("search-history/stock")
    @Operation(description = "주식 종목별 검색량 조회")
    public ResponseEntity<?> gethistoryStock


}
