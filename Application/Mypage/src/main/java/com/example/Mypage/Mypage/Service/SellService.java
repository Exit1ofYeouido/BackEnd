package com.example.Mypage.Mypage.Service;

import static com.example.Mypage.Mypage.Service.AccountService.MARKET_TABLE_ID;
import static com.example.Mypage.Mypage.Service.AccountService.PENDING_TABLE_ID;

import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Entity.AccountHistory;
import com.example.Mypage.Common.Entity.CompletedStockSale;
import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Common.Entity.SaleInfo;
import com.example.Mypage.Common.Entity.StockSaleRequest;
import com.example.Mypage.Common.Entity.StockSaleRequestA;
import com.example.Mypage.Common.Entity.StockSaleRequestB;
import com.example.Mypage.Common.Entity.StockTradeHistory;
import com.example.Mypage.Common.Repository.AccountHistoryRepository;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Common.Repository.CompletedStockSaleRepository;
import com.example.Mypage.Common.Repository.MemberRepository;
import com.example.Mypage.Common.Repository.MemberStockRepository;
import com.example.Mypage.Common.Repository.SaleInfoRepository;
import com.example.Mypage.Common.Repository.StockSaleRequestARepository;
import com.example.Mypage.Common.Repository.StockSaleRequestBRepository;
import com.example.Mypage.Common.Repository.TradeRepository;
import com.example.Mypage.Common.Sms.MessageUtil;
import com.example.Mypage.Mypage.Dto.in.StockSellRequestDto;
import com.example.Mypage.Mypage.Dto.out.StocksSellResponseDto;
import com.example.Mypage.Mypage.Exception.AccountNotFoundException;
import com.example.Mypage.Mypage.Exception.ExceedSaleAmountException;
import com.example.Mypage.Mypage.Webclient.Service.ApiService;
import com.example.Mypage.Mypage.Webclient.handler.StockPriceSocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellService {

    public static final String CLOSE_MARKET = "PINGPONG";
    public static final int TEN_SECOND = 10000;
    public static final int OPEN = 1;
    public static final int CLOSE = 0;
    public static final int SALE_TABLE_A = 0;
    public static final String OPEN_STATUS = "005930";
    private static final String SALE_MESSAGE_TEMPLATE = "[StockCraft] 체결 알리미 \n 종목명 : %s\n 매도수량 : %s\n 판매금액 : %s원";

    private final StockSaleRequestARepository stockSaleRequestARepository;
    private final StockSaleRequestBRepository stockSaleRequestBRepository;
    private final MemberStockRepository memberStockRepository;
    private final SaleInfoRepository saleInfoRepository;
    private final CompletedStockSaleRepository completedStockSaleRepository;
    private final ApiService apiService;
    private final TradeRepository tradeRepository;
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final MessageUtil messageUtil;

    @Value("${approval.uri}")
    private String stockPriceURI;
    private WebSocketConnectionManager connectionManager;

    @Autowired
    private StockPriceSocketHandler stockPriceSocketHandler;
    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public StocksSellResponseDto saveStockSellRequest(Long memberId, StockSellRequestDto stockSellRequestDto) {

        String enterpriseName = stockSellRequestDto.getStockName();

        if (stockSellRequestDto.getSellAmount() <= 0) {
            throw new ExceedSaleAmountException("보유한 주식을 초과하여 판매할 수 없습니다.");
        }

        MemberStock memberStock = memberStockRepository.findByStockNameAndMember(enterpriseName, memberId);

        if (memberStock == null) {
            throw new NoSuchElementException("보유한 주식만 판매가능합니다.");
        }

        Member member = memberStock.getMember();

        double canSellAmount = memberStock.getAvailableAmount() - stockSellRequestDto.getSellAmount();
        if (canSellAmount < 0) {
            throw new ExceedSaleAmountException("보유한 주식을 초과하여 판매할 수 없습니다.");
        }

        memberStock.setAvailableAmount(canSellAmount);
        memberStockRepository.save(memberStock);
        DecimalFormat decimalFormat = new DecimalFormat("#.######");

        saveSellRequest(stockSellRequestDto, member);

        return StocksSellResponseDto.builder()
                .requestAmount(stockSellRequestDto.getSellAmount())
                .remainAmount(decimalFormat.format(memberStock.getAvailableAmount()))
                .build();
    }

    @Scheduled(cron = "0 0 10,15 * * mon-fri")
    @Transactional
    public void processSellRequest() {
        SaleInfo marketInfo = saleInfoRepository.findById(MARKET_TABLE_ID)
                .orElseThrow(() -> new NoSuchElementException("영업 여부 판단 불가"));
        SaleInfo pendingTable = saleInfoRepository.findById(PENDING_TABLE_ID)
                .orElseThrow(() -> new NoSuchElementException("판매대기 테이블 확인 불가"));

        if ((marketInfo.getIdx()) == CLOSE) {
            return;
        }

        int currentSaleTable = (pendingTable.getIdx() + 1) % 2;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soldTime = now.withMinute(0).withSecond(0).withNano(0);

        log.info("{} :: 소수점 주식 정산시작", now);

        try {
            if (currentSaleTable == SALE_TABLE_A) {
                List<StockSaleRequestA> sellRequests = stockSaleRequestARepository.findAll();
                processStockSellRequests(sellRequests, soldTime);
                stockSaleRequestARepository.deleteAll();
            } else {
                List<StockSaleRequestB> sellRequests = stockSaleRequestBRepository.findAll();
                processStockSellRequests(sellRequests, soldTime);
                stockSaleRequestBRepository.deleteAll();
            }
            log.info("{} :: 소수점 주식 판매 종합 및 정산 완료", now);

        } catch (Exception e) {
            //TODO: 커스텀 Exception 처리하기
            log.error("{} :: 소수점 판매 취합과정에서 오류가 발생했습니다. => {}", LocalDateTime.now(), e.getMessage());
        }

    }

    @Transactional
    @Scheduled(cron = "30 0 9 * * *")
    public void updateTodayMarketStatus() {
        String approvalKey = apiService.updateMarketAccessKey();

        stockPriceSocketHandler = new StockPriceSocketHandler(approvalKey, OPEN_STATUS);

        if (connectionManager == null) {
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            connectionManager = new WebSocketConnectionManager(webSocketClient, stockPriceSocketHandler,
                    stockPriceURI);
            connectionManager.setAutoStartup(true);
            connectionManager.start();
        }

        SaleInfo saleInfo = saleInfoRepository.findById(MARKET_TABLE_ID)
                .orElseThrow(() -> new IllegalStateException("SaleInfo Table 상태를 확인하세요."));
        if (isOpenStockMarket()) {
            saleInfo.setIdx(1);
        } else {
            saleInfo.setIdx(0);
        }
        saleInfoRepository.save(saleInfo);
        log.info("{} :: update Today marketStatus => {}", LocalDateTime.now(), saleInfo.getIdx());
    }

    @Scheduled(cron = "0 30 9,14 * * mon-fri")
    @Transactional
    public void changeSaleTableIdx() {
        int todayMarketStatus = saleInfoRepository.findById(MARKET_TABLE_ID).get().getIdx();
        SaleInfo saleInfo = saleInfoRepository.findById(PENDING_TABLE_ID).get();

        if (todayMarketStatus == OPEN) {
            saleInfo.setIdx((saleInfo.getIdx() + 1) % 2);
            saleInfoRepository.save(saleInfo);
        }
        log.info("{} :: current saleTableIdx => {}", LocalDateTime.now(), todayMarketStatus);
    }

    private boolean isOpenStockMarket() {
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(TEN_SECOND);
                String response = stockPriceSocketHandler.getLatestMessage();
                String extractTrId = extractTrId(response);

                if (extractTrId.equals(CLOSE_MARKET)) {
                    log.info("{} :: 오늘은 비영업일 입니다.", LocalDateTime.now());
                    connectionManager.stop();
                    return false;
                }
            }
            connectionManager.stop();
            log.info("{} :: 영업일 입니다.", LocalDateTime.now());
            return true;

        } catch (InterruptedException e) {
            log.error("주식 영업일 확인중 오류 발생 => {}", e.getMessage());
            return false;
        }
    }

    private void saveSellRequest(StockSellRequestDto stockSellRequestDto, Member member) {
        SaleInfo pendingTable = saleInfoRepository.findById(PENDING_TABLE_ID)
                .orElseThrow(() -> new NoSuchElementException("판매대기 테이블 확인 불가"));

        if (pendingTable.getIdx() == SALE_TABLE_A) {
            StockSaleRequestA saveStockSaleRequestA = stockSellRequestDto.toSellRequestA(member);
            stockSaleRequestARepository.save(saveStockSaleRequestA);
            log.info("StockSaleRequestA 저장 성공 => {}", saveStockSaleRequestA);
        } else {
            stockSaleRequestBRepository.save(stockSellRequestDto.toSellRequestB(member));
            StockSaleRequestB saveStockSaleRequestB = stockSellRequestDto.toSellRequestB(member);
            log.info("StockSaleRequestB 저장 성공 => {}", saveStockSaleRequestB);
        }
    }

    private String extractTrId(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return jsonNode.path("header").path("tr_id").asText();
        } catch (Exception e) {
            String stockCode = jsonString.substring(jsonString.length() - 6);
            if (stockCode.equals(OPEN_STATUS)) {
                return OPEN_STATUS;
            }
            throw new NoSuchElementException("실시간 호가 추출과정에서 오류가 발생했습니다.");
        }
    }

    private <T extends StockSaleRequest> void processStockSellRequests(List<T> sellRequests, LocalDateTime soldTime) {
        for (T sellRequest : sellRequests) {
            String curStockCode = sellRequest.getStockCode();
            CompletedStockSale completedStockSale = completedStockSaleRepository.findByStockCodeAndSoldTime(
                    curStockCode, soldTime);

            int curStockPrice = 0;

            if (completedStockSale == null) {
                curStockPrice = apiService.getPrice(curStockCode);
                completedStockSaleRepository.save(newSellStock(sellRequest, curStockPrice, soldTime));
            } else {
                curStockPrice = completedStockSale.getSoldPrice();
                updateExistingStock(completedStockSale, sellRequest.getAmount());
            }

            Long memberId = sellRequest.getMember().getId();

            Account memberAccount = accountRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new AccountNotFoundException("포인트를 지급할 계좌 탐색과정에서 오류 발생"));
            int sellPrice = getSellPrice(sellRequest, curStockPrice);
            int afterHoldPoint = memberAccount.getPoint() + sellPrice;

            memberAccount.setPoint(afterHoldPoint);
            accountRepository.save(memberAccount);
            accountHistoryRepository.save(newAccountHistory(sellRequest, sellPrice, afterHoldPoint, memberAccount));

            updateMemberStockInfo(memberId, sellRequest);

            String message = String.format(
                    SALE_MESSAGE_TEMPLATE,
                    sellRequest.getEnterpriseName(),
                    formatSellAmount(sellRequest.getAmount()),
                    formatSellPrice(sellPrice)
            );

            messageUtil.sendMessage(
                    message,
                    sellRequest.getMember().getPhoneNumber());

        }
    }

    private static @NotNull String formatSellPrice(int sellPrice) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(sellPrice);
    }

    private static <T extends StockSaleRequest> AccountHistory newAccountHistory(T sellRequest, int sellPrice,
                                                                                 int afterHoldPoint,
                                                                                 Account memberAccount) {
        return AccountHistory.builder()
                .requestPoint(sellPrice)
                .resultPoint(afterHoldPoint)
                .type("in")
                .createdAt(LocalDateTime.now())
                .account(memberAccount)
                .member(sellRequest.getMember())
                .build();
    }

    @Transactional
    public void updateMemberStockInfo(Long memberId, StockSaleRequest sellRequest) {
        MemberStock memberStock = memberStockRepository.findByMemberIdAndStockCode(memberId,
                        sellRequest.getStockCode())
                .orElseThrow(() -> new AccountNotFoundException("유저의 보유주식 탐색과정에서 오류가 발생했습니다."));

        memberStock.setAmount(memberStock.getAmount() - sellRequest.getAmount());
        memberStockRepository.save(memberStock);

        tradeRepository.save(StockTradeHistory.builder()
                .memberStock(memberStock)
                .member(sellRequest.getMember())
                .stockName(sellRequest.getEnterpriseName())
                .amount(sellRequest.getAmount())
                .createdAt(LocalDateTime.now())
                .tradeType("out")
                .build()
        );
    }

    private <T extends StockSaleRequest> int getSellPrice(T sellRequest, double curStockPrice) {
        return (int) (curStockPrice * sellRequest.getAmount());
    }

    private CompletedStockSale newSellStock(StockSaleRequest sellRequest, int curStockPrice,
                                            LocalDateTime soldTime) {
        return CompletedStockSale.builder()
                .soldPrice(curStockPrice)
                .amount(sellRequest.getAmount())
                .soldTime(soldTime)
                .enterpriseName(sellRequest.getEnterpriseName())
                .stockCode(sellRequest.getStockCode())
                .build();
    }

    private void updateExistingStock(CompletedStockSale completedStockSale, double additionalAmount) {
        completedStockSale.setAmount(completedStockSale.getAmount() + additionalAmount);
        completedStockSaleRepository.save(completedStockSale);
    }

    private String formatSellAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######");
        return decimalFormat.format(amount);
    }
}

