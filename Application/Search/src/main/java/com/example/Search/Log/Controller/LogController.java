package com.example.Search.Log.Controller;

import com.example.Search.Log.Dto.out.GetDate;
import com.example.Search.Log.Dto.out.GetHistoryStockResponseDto;
import com.example.Search.Log.Dto.out.GetSearchLogMemberStockDto;
import com.example.Search.Log.Dto.out.MemberStockCountDto;
import com.example.Search.Log.Service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="Log API")
@RequestMapping("/admin")
@Slf4j
public class LogController {

    private final LogService logService;

    @GetMapping("/search-history/stock")
    @Operation(description = "주식 종목별 검색량 조회")
    public ResponseEntity<?> gethistoryStock(@RequestParam("enterpriseName") String enterpriseName,@RequestParam("year") String year
            ,@RequestParam("month") String month) throws ParseException {


        List<GetHistoryStockResponseDto> getHistoryStockResponseDtos=logService.gethistoryStock(enterpriseName,year,month);

        return ResponseEntity.ok(getHistoryStockResponseDtos);
    }

    @GetMapping("/search-history/member-stock")
    @Operation(description = "주식 종목, 회원 지정하여 검색량 조회")
    public ResponseEntity<GetSearchLogMemberStockDto> getSearchLogMemberStock(@RequestParam("memberId") String memberId, @RequestParam("enterpriseName") String enterpriseName, @RequestParam("year") String year, @RequestParam("month") String month) {
        GetSearchLogMemberStockDto getSearchLogMemberStockDto = logService.getLogMemberStock(Long.valueOf(memberId), enterpriseName, Integer.parseInt(year), Integer.parseInt(month));
        return ResponseEntity.ok(getSearchLogMemberStockDto);
    }


}
