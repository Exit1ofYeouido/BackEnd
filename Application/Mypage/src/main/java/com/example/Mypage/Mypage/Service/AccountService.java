package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Entity.AccountHistory;
import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Common.Entity.SaleInfo;
import com.example.Mypage.Common.Entity.StockSaleRequest;
import com.example.Mypage.Common.Entity.StockTradeHistory;
import com.example.Mypage.Common.Repository.AccountHistoryRepository;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Common.Repository.MemberStockRepository;
import com.example.Mypage.Common.Repository.SaleInfoRepository;
import com.example.Mypage.Common.Repository.StockSaleRequestARepository;
import com.example.Mypage.Common.Repository.StockSaleRequestBRepository;
import com.example.Mypage.Common.Repository.TradeRepository;
import com.example.Mypage.Mypage.Dto.out.GetPointHistoryResponseDto;
import com.example.Mypage.Mypage.Dto.out.GetPointResponseDto;
import com.example.Mypage.Mypage.Dto.out.MyStockSaleRequestResponseDto;
import com.example.Mypage.Mypage.Dto.out.MyStockSaleRequestsResponseDto;
import com.example.Mypage.Mypage.Dto.out.MyStocksHistoryResponseDto;
import com.example.Mypage.Mypage.Dto.out.MyStocksResponseDto;
import com.example.Mypage.Mypage.Exception.AccountNotFoundException;
import com.example.Mypage.Mypage.Exception.BadRequestException;
import com.example.Mypage.Mypage.Webclient.Service.ApiService;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final ApiService apiService;
    private final MemberStockRepository memberStockRepository;
    private final TradeRepository tradeRepository;
    private final StockSaleRequestARepository stockSaleRequestARepository;
    private final StockSaleRequestBRepository stockSaleRequestBRepository;
    private final SaleInfoRepository saleInfoRepository;

    public GetPointResponseDto getPoint(Long memberId) {
        try {
            Account account = accountRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
            return GetPointResponseDto.builder()
                    .point(account.getPoint())
                    .build();
        } catch (AccountNotFoundException e) {
            log.error("유효한 계좌를 찾을 수 없습니다. {}", e.getMessage());
            throw e;
        }
    }

    public List<GetPointHistoryResponseDto> getPointHistory(Long memberId, int index, int limit) {
        Pageable pageable = PageRequest.of(index, limit);
        Page<AccountHistory> accountHistoryPage = accountHistoryRepository.findByMemberId(memberId, pageable);
        List<AccountHistory> accountHistoryList = accountHistoryPage.getContent();

        return getPointHistoryResponseDtos(accountHistoryList);
    }

    public List<MyStocksResponseDto> getAllMyStocks(Long memberId) {
        log.info("MemberId : {} 의 보유주식 조회", memberId);
        List<MemberStock> memberStocks = memberStockRepository.findByMemberId(memberId);
        List<MyStocksResponseDto> myStocks = new ArrayList<>();

        for (MemberStock memberStock : memberStocks) {
            myStocks.add(MyStocksResponseDto.builder()
                    .name(memberStock.getStockName())
                    .earningRate(getEarningRate(memberStock))
                    .holdStockCount(memberStock.getAmount())
                    .build());
        }
        return myStocks;
    }

    public List<MyStocksHistoryResponseDto> getMyStocksHistory(Long memberId, int index, int limit) {
        Pageable pageable = PageRequest.of(index, limit);
        Page<StockTradeHistory> myStockHistoyPage = tradeRepository.findByMemberId(memberId, pageable);
        List<StockTradeHistory> stockTradeHistories = myStockHistoyPage.getContent();

        return stockTradeHistories.stream()
                .map(stockTradeHistory -> MyStocksHistoryResponseDto.builder()
                        .name(stockTradeHistory.getStockName())
                        .type(stockTradeHistory.getTradeType())
                        .amount(String.format("%.6f", stockTradeHistory.getAmount()))
                        .date(stockTradeHistory.getCreatedAt()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")))
                        .build())
                .toList();
    }

    public MyStockSaleRequestsResponseDto getMyStocksSaleRequests(Long memberId) {
        SaleInfo saleInfo = saleInfoRepository.findById(1)
                .orElseThrow(() -> new NoSuchElementException("pending table idx를 찾을 수 없습니다."));
        int index = (saleInfo.getIdx() + 1) % 2;

        List<? extends StockSaleRequest> stockSaleRequests;

        if (index == 0) {
            stockSaleRequests = stockSaleRequestARepository.findAllByMemberId(memberId);
        } else {
            stockSaleRequests = stockSaleRequestBRepository.findAllByMemberId(memberId);
        }
        List<MyStockSaleRequestResponseDto> responseDtos = stockSaleRequests.stream()
                .map(MyStockSaleRequestResponseDto::new)
                .collect(Collectors.toList());

        return new MyStockSaleRequestsResponseDto(responseDtos);
    }

    @Transactional
    public boolean deleteMyStocksSaleRequest(Long saleId, Long memberId) {
        SaleInfo saleInfo = saleInfoRepository.findById(1)
                .orElseThrow(() -> new NoSuchElementException("pending table idx를 찾을 수 없습니다."));

        StockSaleRequest stockSaleRequest;

        int index = (saleInfo.getIdx() + 1) % 2;

        if (index == 0) {
            stockSaleRequest = stockSaleRequestARepository.findById(saleId)
                    .orElseThrow(() -> new BadRequestException("보유한 주식만 취소요청이 가능합니다."));
            stockSaleRequestARepository.deleteById(saleId);
        } else {
            stockSaleRequest = stockSaleRequestBRepository.findById(saleId)
                    .orElseThrow(() -> new BadRequestException("보유한 주식만 취소요청이 가능합니다."));
            stockSaleRequestBRepository.deleteById(saleId);
        }

        MemberStock memberStock = memberStockRepository.findByMemberIdAndStockCode(memberId,
                        stockSaleRequest.getStockCode())
                .orElseThrow(() -> new NoSuchElementException("판매를 취소과정에서 오류가 발생했습니다."));

        memberStock.setAvailableAmount(memberStock.getAvailableAmount() + stockSaleRequest.getAmount());
        return true;
    }

    private String getEarningRate(MemberStock memberStock) {
        int curPrice = apiService.getPrice(memberStock.getStockCode());
        double resultPrice = (double) curPrice / (double) memberStock.getAveragePrice();

        double earningRate = (resultPrice - 1) * 100;

        if (resultPrice < 1) {
            earningRate = (1 - resultPrice) * 100;
        }

        return String.format("%.2f", earningRate);
    }

    private static List<GetPointHistoryResponseDto> getPointHistoryResponseDtos(
            List<AccountHistory> accountHistoryList) {
        return accountHistoryList.stream()
                .map(accountHistory -> GetPointHistoryResponseDto.builder()
                        .memberId(accountHistory.getMember().getId())
                        .requestPoint(accountHistory.getRequestPoint())
                        .resultPoint(accountHistory.getResultPoint())
                        .type(accountHistory.getType())
                        .createdAt(accountHistory.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
