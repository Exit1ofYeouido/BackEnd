package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Entity.AccountHistory;
import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Common.Entity.Trade;
import com.example.Mypage.Common.Repository.AccountHistoryRepository;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Common.Repository.MemberStockRepository;
import com.example.Mypage.Common.Repository.TradeRepository;
import com.example.Mypage.Mypage.Dto.out.GetPointHistoryResponseDto;
import com.example.Mypage.Mypage.Dto.out.GetPointResponseDto;
import com.example.Mypage.Mypage.Dto.out.MyStocksHistoryResponseDto;
import com.example.Mypage.Mypage.Dto.out.MyStocksResponseDto;
import com.example.Mypage.Mypage.Exception.AccountNotFoundException;
import com.example.Mypage.Mypage.Webclient.Service.ApiService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final ApiService apiService;
    private final MemberStockRepository memberStockRepository;
    private final TradeRepository tradeRepository;

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
                    .earningRate(getEarningRate(memberStock))
                    .holdStockCount(memberStock.getCount())
                    .build());
        }

        return myStocks;
    }

    public List<MyStocksHistoryResponseDto> getMyStocksHistory(Long memberId, int index, int limit) {
        Pageable pageable = PageRequest.of(index, limit);
        Page<Trade> myStockHistoyPage = tradeRepository.findByMemberId(memberId, pageable);
        List<Trade> trades = myStockHistoyPage.getContent();

        return trades.stream()
                .map(trade -> MyStocksHistoryResponseDto.builder()
                        .name(trade.getStockName())
                        .type(trade.getTradeType())
                        .amount(String.format("%.6f", trade.getCount()))
                        .date((trade.getCreatedAt())
                        ).build())
                .toList();
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
