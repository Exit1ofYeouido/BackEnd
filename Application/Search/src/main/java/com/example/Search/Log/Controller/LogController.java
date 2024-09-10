package com.example.Search.Log.Controller;

import com.example.Search.Log.Dto.out.GetSearchLogMemberStockDto;
import com.example.Search.Log.Dto.out.MemberCountDto;
import com.example.Search.Log.Dto.out.StockCountResponseDto;
import com.example.Search.Log.Service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Log API")
@RequestMapping("/admin")
@Slf4j
public class LogController {

    private final LogService logService;

    @GetMapping("")
    @Operation(description = "관리자페이지 권한 인증")
    public ResponseEntity<?> checkMyRole(@RequestHeader("role") String role) {
        logService.isAdmin(role);
        return ResponseEntity.ok("인증된 사용자입니다.");
    }

    @GetMapping("/search-history/member-stock")
    @Operation(description = "주식 종목, 회원 지정하여 검색량 조회")
    public ResponseEntity<GetSearchLogMemberStockDto> getSearchLogMemberStock(
            @RequestHeader("role") String role, @RequestParam("memberId") String memberId,
            @RequestParam("enterpriseName") String enterpriseName,
            @RequestParam("year") String year,
            @RequestParam("month") String month) {
        GetSearchLogMemberStockDto getSearchLogMemberStockDto = logService.getLogMemberStock(
                role, Long.valueOf(memberId),
                enterpriseName, Integer.parseInt(year), Integer.parseInt(month));
        return ResponseEntity.ok(getSearchLogMemberStockDto);
    }

    @GetMapping("/search-history/member")
    @Operation(description = "회원 지정하여 주식별 검색량 조회")

    public ResponseEntity<List<MemberCountDto>> getSearchLogMember(@RequestHeader("role") String role,
                                                                   @RequestParam("memberId") String memberId,
                                                                   @RequestParam("year") String year,
                                                                   @RequestParam("month") String month) {
        List<MemberCountDto> memberCountDtos = logService.getLogMember(role, Long.valueOf(memberId),
                Integer.parseInt(year),
                Integer.parseInt(month));
        return ResponseEntity.ok(memberCountDtos);
    }

    @GetMapping("/search-history/stock")
    @Operation(description = "주식 지정하여 날짜별, 월별 검색량 조회")
    public ResponseEntity<StockCountResponseDto> getSearchLogStock(
            @RequestHeader("role") String role,
            @RequestParam("enterpriseInfo") String enterpriseInfo,
            @RequestParam("year") String year,
            @RequestParam("month") String month) {
        StockCountResponseDto stockCountResponseDto = logService.getLogStock(role, enterpriseInfo,
                Integer.parseInt(year),
                Integer.parseInt(month));
-        return ResponseEntity.ok(stockCountResponseDto);
    }
}
