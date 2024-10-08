package com.example.Mypage.Mypage.Controller;

import com.example.Mypage.Mypage.Dto.in.StockSellRequestDto;
import com.example.Mypage.Mypage.Dto.in.WithdrawalRequestDto;
import com.example.Mypage.Mypage.Dto.out.*;
import com.example.Mypage.Mypage.Service.AccountService;
import com.example.Mypage.Mypage.Service.SellService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final SellService sellService;

    @GetMapping("/point")
    @Operation(description = "나의 현재 포인트 조회")
    public ResponseEntity<GetPointResponseDto> getPoint(@RequestHeader("memberId") Long memberId) {
        GetPointResponseDto getPointResponseDto = accountService.getPoint(memberId);
        return ResponseEntity.ok(getPointResponseDto);
    }

    @GetMapping("/point-history")
    @Operation(description = "나의 포인트 변동내역 조회")
    public ResponseEntity<?> getPointHistory(@RequestHeader("memberId") Long memberId,
                                             @RequestParam(defaultValue = "0") int index,
                                             @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(accountService.getPointHistory(memberId, index, size));
    }

    @GetMapping("/pre-withdrawal")
    @Operation(description = "출금금액 요청단계에서 계좌상태 확인 API")
    public ResponseEntity<PreWithdrawalResponseDto> preWithdrawalRequest(@RequestHeader("memberId") Long memberId) {
        PreWithdrawalResponseDto preWithdrawal = accountService.preWithdrawal(memberId);

        return ResponseEntity.ok(preWithdrawal);
    }

    @PostMapping("/withdrawal")
    @Operation(description = "포인트 출금 API")
    public ResponseEntity<WithdrawalResponseDto> withdrawalRequest(@RequestHeader("memberId") Long memberId,
                                                                   @RequestBody
                                                                   WithdrawalRequestDto withdrawalRequestDto) {

        WithdrawalResponseDto withdrawalResponseDto = accountService.withdrawalPoint(memberId, withdrawalRequestDto);
        return ResponseEntity.ok(withdrawalResponseDto);
    }

    @GetMapping("/stocks-value")
    @Operation(description = "나의 주식가치 조회")
    public ResponseEntity<StocksValueResponseDto> getStocksValue(@RequestHeader("memberId") Long memberId) {
        StocksValueResponseDto stocksValueResponseDto = accountService.getCurrentStocksValue(memberId);
        return ResponseEntity.ok(stocksValueResponseDto);
    }

    @GetMapping("/stocks")
    @Operation(description = "나의 보유주식 조회")
    public ResponseEntity<MyStocksPageResponseDto> getMyStocks(@RequestHeader("memberId") Long memberId) {
        return ResponseEntity.ok(accountService.getAllMyStocks(memberId));
    }

    @GetMapping("/stocks-history")
    @Operation(description = "나의 주식 거래내역 조회")
    public ResponseEntity<StocksHistoryResponseDto> getMyStocksHistory(@RequestHeader("memberId") Long memberId,
                                                                       @RequestParam(defaultValue = "0") int index,
                                                                       @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(accountService.getMyStocksHistory(memberId, index, size));
    }

    @GetMapping("/stocks/pre-sell/{stockCode}")
    @Operation(description = "나의 주식 판매수량 조건확인 api")
    public ResponseEntity<StockSaleConditionResponseDto> getMyStockSellCondition(
            @RequestHeader("memberId") Long memberId, @PathVariable String stockCode) {
        StockSaleConditionResponseDto stockSaleConditionResponseDto = accountService.getCurrentStocksSellCondition(
                memberId, stockCode);
        return ResponseEntity.ok(stockSaleConditionResponseDto);
    }

    @PostMapping("/stocks/sell")
    @Operation(description = "나의 주식 판매하기")
    public ResponseEntity<StocksSellResponseDto> sellMyStock(@RequestHeader("memberId") Long memberId,
                                                             @RequestBody StockSellRequestDto stockSellRequestDto) {
        StocksSellResponseDto stocksSellResponseDto = sellService.saveStockSellRequest(memberId, stockSellRequestDto);

        return new ResponseEntity<>(stocksSellResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/stocks/pending")
    @Operation(description = "나의 소수점 주식 판매대기 목록 조회")
    public ResponseEntity<MyStockSaleRequestsResponseDto> getMySellPendingStocks(
            @RequestHeader("memberId") Long memberId) {
        MyStockSaleRequestsResponseDto myStockSaleRequestsResponseDto = accountService.getMyStocksSaleRequests(
                memberId);
        return new ResponseEntity<>(myStockSaleRequestsResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/stocks/pending/{saleId}")
    @Operation(description = "나의 소수점 주식 판매요청 취소")
    public ResponseEntity<String> cancelMySellPendingStocks(@RequestHeader("memberId") Long memberId,
                                                            @PathVariable("saleId") Long saleId) {
        accountService.deleteMyStocksSaleRequest(saleId, memberId);
        return ResponseEntity.ok("return ResponseEntity.ok(\"정상적으로 주문 취소처리되었습니다.\");");
    }

    @PostMapping("/stocks/purchase")
    @Operation(description = "구매하기 버튼 ")
    public ResponseEntity<?> purchaseStocks(@RequestHeader("memberId") Long memberId, HttpServletRequest request){

        String agent=request.getHeader("USER-AGENT");

        GetPurchaseStocksResponseDto getpurcahseStocks=accountService.getPurcahseStocks(agent.toLowerCase());
        return ResponseEntity.ok(getpurcahseStocks);
    }
}
