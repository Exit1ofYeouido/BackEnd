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
import com.example.Mypage.Mypage.Dto.in.WithdrawalRequestDto;
import com.example.Mypage.Mypage.Dto.out.*;
import com.example.Mypage.Mypage.Exception.AccountNotFoundException;
import com.example.Mypage.Mypage.Exception.BadRequestException;
import com.example.Mypage.Mypage.Exception.InValidStockCodeException;
import com.example.Mypage.Mypage.Webclient.Service.ApiService;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
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

    public static final int PENDING_TABLE_ID = 1;
    public static final int MARKET_TABLE_ID = 2;
    public static final double MIN_SALE_PRICE = 1000;

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

    public PointHistoryResponseDto getPointHistory(Long memberId, int index, int limit) {
        Pageable pageable = PageRequest.of(index, limit);
        Page<AccountHistory> accountHistoryPage = accountHistoryRepository.findByMemberId(memberId, pageable);
        List<AccountHistory> accountHistoryList = accountHistoryPage.getContent();

        int size = (int) accountHistoryPage.getTotalElements();

        return PointHistoryResponseDto.builder()
                .pointHistory(getPointHistoryResponseDtos(accountHistoryList))
                .size(size).build();
    }

    public StocksValueResponseDto getCurrentStocksValue(Long memberId) {
        List<MemberStock> memberStocks = memberStockRepository.findByMemberId(memberId);

        if (memberStocks.isEmpty()) {
            return StocksValueResponseDto.builder()
                    .stocksValue(0)
                    .earningRate("0")
                    .build();
        }

        double preValue = 0;
        double currentValue = 0;

        for (MemberStock memberStock : memberStocks) {
            double stockAmount = memberStock.getAmount();
            double stockAveragePrice = memberStock.getAveragePrice();
            double currentPrice = apiService.getPrice(memberStock.getStockCode());

            preValue += stockAveragePrice * stockAmount;
            currentValue += currentPrice * stockAmount;
        }

        double earningRate = (currentValue - preValue) / preValue * 100;

        return StocksValueResponseDto.builder()
                .stocksValue((int) currentValue)
                .earningRate(formatEarningRate(earningRate))
                .build();
    }

    public MyStocksPageResponseDto getAllMyStocks(Long memberId) {
        log.info("MemberId : {} 의 보유주식 조회", memberId);
        List<MemberStock> memberStocks = memberStockRepository.findByMemberId(memberId);
        List<MyStocksResponseDto> myStocks = new ArrayList<>();

        double preValue = 0;
        double currentValue = 0;

        for (MemberStock memberStock : memberStocks) {
            double currentPrice = apiService.getPrice(memberStock.getStockCode());
            double stockAmount = memberStock.getAmount();
            double stockAveragePrice = memberStock.getAveragePrice();

            preValue += stockAveragePrice * stockAmount;
            currentValue += currentPrice * stockAmount;

            myStocks.add(MyStocksResponseDto.builder()
                    .name(memberStock.getStockName())
                    .earningRate(getEarningRate(memberStock, (int) currentPrice))
                    .stockCode(memberStock.getStockCode())
                    .averagePrice(memberStock.getAveragePrice())
                    .holdStockCount(memberStock.getAmount())
                    .build());
        }

        double earningRate = (currentValue - preValue) / preValue * 100;

        return MyStocksPageResponseDto.builder()
                .myStocksResponse(myStocks)
                .stocksValueResponseDto(StocksValueResponseDto.builder()
                        .stocksValue((int) currentValue)
                        .earningRate(formatEarningRate(earningRate))
                        .build()).build();
    }

    @Transactional
    public StockSaleConditionResponseDto getCurrentStocksSellCondition(Long memberId, String stockCode) {
        int curStockPrice = apiService.getPrice(stockCode);

        if (curStockPrice == 0) {
            log.error("주식 코드 {} 조회 요청 , 현재가를 판단할 수 없음", stockCode);
            throw new InValidStockCodeException("해당 주식은 저희 서비스에서 제공하지 않습니다.");
        }

        double minSaleAmount = MIN_SALE_PRICE / (double) curStockPrice;

        MemberStock memberStock = memberStockRepository.findByMemberIdAndStockCode(memberId, stockCode).orElse(null);

        return createResponseDto(memberStock, minSaleAmount);
    }

    public StocksHistoryResponseDto getMyStocksHistory(Long memberId, int index, int limit) {
        Pageable pageable = PageRequest.of(index, limit);
        Page<StockTradeHistory> myStockHistoyPage = tradeRepository.findByMemberId(memberId, pageable);
        List<StockTradeHistory> stockTradeHistories = myStockHistoyPage.getContent();

        int size = (int) myStockHistoyPage.getTotalElements();

        return StocksHistoryResponseDto.builder().stocksHistory(stockTradeHistories.stream()
                        .map(stockTradeHistory -> MyStocksHistoryResponseDto.builder()
                                .name(stockTradeHistory.getStockName())
                                .type(stockTradeHistory.getTradeType())
                                .amount(String.format("%.6f", stockTradeHistory.getAmount()))
                                .date(stockTradeHistory.getCreatedAt()
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")))
                                .build())
                        .toList())
                .size(size).build();
    }

    public MyStockSaleRequestsResponseDto getMyStocksSaleRequests(Long memberId) {
        SaleInfo saleInfo = saleInfoRepository.findById(PENDING_TABLE_ID)
                .orElseThrow(() -> new NoSuchElementException("pending table idx를 찾을 수 없습니다."));
        int index = saleInfo.getIdx();

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
    public void deleteMyStocksSaleRequest(Long saleId, Long memberId) {
        SaleInfo saleInfo = saleInfoRepository.findById(PENDING_TABLE_ID)
                .orElseThrow(() -> new NoSuchElementException("pending table idx를 찾을 수 없습니다."));

        StockSaleRequest stockSaleRequest;

        int index = saleInfo.getIdx();

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
    }

    public PreWithdrawalResponseDto preWithdrawal(Long memberId) {
        Account account = accountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AccountNotFoundException("계좌개설을 진행하세요."));

        return PreWithdrawalResponseDto.builder()
                .accountNumber(account.getAccountNumber())
                .totalPoint(account.getPoint())
                .build();
    }

    @Transactional
    public WithdrawalResponseDto withdrawalPoint(Long memberId, WithdrawalRequestDto withdrawalRequestDto) {
        Account account = accountRepository.findByAccountNumber(withdrawalRequestDto.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌개설을 진행하세요."));

        int myPoint = account.getPoint();
        int requestPoint = withdrawalRequestDto.getWithdrawalAmount();

        if (requestPoint > myPoint) {
            throw new BadRequestException("보유 포인트를 초과하여 출금할 수 없습니다.");
        }

        int resultPoint = myPoint - requestPoint;
        account.setPoint(resultPoint);
        accountRepository.save(account);

        AccountHistory accountHistory = AccountHistory.builder()
                .account(account)
                .member(account.getMember())
                .requestPoint(requestPoint)
                .resultPoint(resultPoint)
                .type("out")
                .createdAt(LocalDateTime.now())
                .build();
        accountHistoryRepository.save(accountHistory);

        return WithdrawalResponseDto.builder().remainPoint(account.getPoint()).build();
    }

    private String getEarningRate(MemberStock memberStock, int currentPrice) {
        double resultPrice = (double) currentPrice / (double) memberStock.getAveragePrice();

        if (resultPrice == 0) {
            return "0";
        }

        double earningRate = (resultPrice - 1) * 100;

        if (resultPrice < 1) {
            earningRate = (1 - resultPrice) * 100;
            return String.format("%.2f", -earningRate);
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
                        .date(accountHistory.getCreatedAt()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")))
                        .build())
                .collect(Collectors.toList());
    }

    private StockSaleConditionResponseDto createResponseDto(MemberStock memberStock, double minSaleAmount) {
        String holdingAmount = memberStock != null ? formatSellAmount(memberStock.getAvailableAmount()) : "0";

        boolean isSellable = memberStock != null;
        if (isSellable) {
            isSellable = memberStock.getAvailableAmount() > minSaleAmount;
        }
        return StockSaleConditionResponseDto.builder()
                .holdingAmount(holdingAmount)
                .minSaleAmount(formatSellAmount(minSaleAmount))
                .isSellable(isSellable)
                .build();
    }

    private String formatSellAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        return decimalFormat.format(amount);
    }

    private String formatEarningRate(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(amount);
    }

    public GetPurchaseStocksResponseDto getPurcahseStocks(String agent) {

        if (agent.contains("android") ){
            return GetPurchaseStocksResponseDto
                    .builder()
                    .url("https://play.google.com/store/apps/details?id=com.shinhaninvest.nsmts")
                    .build();
        }
        else if(agent.contains("iphone") || agent.contains("ipad") ){
            return GetPurchaseStocksResponseDto
                    .builder()
                    .url("https://apps.apple.com/kr/app/%EC%8B%A0%ED%95%9C-sol%EC%A6%9D%EA%B6%8C-%EB%8C%80%ED%91%9Cmts/id1168512940")
                    .build();

        }

        return GetPurchaseStocksResponseDto
                .builder()
                .url("https://www.shinhansec.com/WEB-APP/wts/main/index.cmd?screen=1402")
                .build();
    }
}
