package com.example.Home.HomeService;

import com.example.Home.HomeDTO.HomeResponseDTO;
import com.example.Home.Kis.KoreaInvestmentApiService;
import com.example.Home.Common.Entity.Member;
import com.example.Home.Common.Entity.Account;
import com.example.Home.Common.Entity.MemberStock;
import com.example.Home.Common.Repository.AccountRepository;
import com.example.Home.Common.Repository.MemberRepository;
import com.example.Home.Common.Repository.MemberStockRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HomeService {

    private final MemberRepository memberRepository;
    private final MemberStockRepository memberStockRepository;
    private final AccountRepository accountRepository;
    private final KoreaInvestmentApiService koreaInvestmentApiService;

    public HomeService(MemberRepository memberRepository, MemberStockRepository memberStockRepository, AccountRepository accountRepository, KoreaInvestmentApiService koreaInvestmentApiService) {
        this.memberRepository = memberRepository;
        this.memberStockRepository = memberStockRepository;
        this.accountRepository = accountRepository;
        this.koreaInvestmentApiService = koreaInvestmentApiService;
    }

    public HomeResponseDTO getHomeData(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버가 없습니다."));
        List<MemberStock> memberStocks = memberStockRepository.findByMemberId(memberId);
        Account account = accountRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("멤버 포인트가 없습니다."));
        int totalPoint = account.getPoint();
        int totalStock = calculateTotalStock(memberStocks);
        int totalAssets = totalPoint + totalStock;
        double totalEarningRate = calculateEarningRate(memberStocks);

        return new HomeResponseDTO(
                totalPoint,
                totalStock,
                totalAssets,
                totalEarningRate,
                "https://example.com/logo.png"
        );
    }

    private int calculateTotalStock(List<MemberStock> memberStocks) {
        Map<String, Long> stockPrices = getStockPrices(memberStocks.stream()
                .map(MemberStock::getCode)
                .collect(Collectors.toList()));

        return memberStocks.stream()
                .mapToInt(stock -> (int) (stock.getCount() * stockPrices.getOrDefault(stock.getCode(), 0L)))
                .sum();
    }

    private Map<String, Long> getStockPrices(List<String> stockCodes) {
        Map<String, Long> stockPrices = stockCodes.stream()
                .collect(Collectors.toMap(
                        code -> code,
                        koreaInvestmentApiService::getCurrentPrice
                ));
        log.debug("getStockPrices result: {}", stockPrices);

        return stockPrices;
    }

    private double calculateEarningRate(List<MemberStock> memberStocks) {
        if(memberStocks.isEmpty()) {return 0.0;}

        Map<String,Long> currentPrices = getStockPrices(memberStocks.stream().map(MemberStock::getCode).collect(Collectors.toList()));
        System.out.println("current prices" + currentPrices);

        double totalCurrentValue = 0.0;
        double totalPurchaseValue = 0.0;

        for (MemberStock memberStock : memberStocks) {
            long currentPrice = currentPrices.getOrDefault(memberStock.getCode(), 0L);
            System.out.println("current price: " + currentPrice);
            long purchasePrice = memberStock.getAveragePrice();
            System.out.println("average price: " + purchasePrice);
            double currentValue = memberStock.getCount() * currentPrice;
            System.out.println("current value: " + currentValue);
            double purchaseValue = memberStock.getCount() * purchasePrice;
            System.out.println("purchase value: " + purchaseValue);

            totalCurrentValue += currentValue;
            System.out.println("api로 불러온 현재가 : " + totalCurrentValue);
            totalPurchaseValue += purchaseValue;
            System.out.println("내가 샀을 때 평균가 : "+ totalPurchaseValue);
        }
        double earningRate = ((totalCurrentValue - totalPurchaseValue) / totalPurchaseValue) * 100;
        return Math.round(earningRate*100.0) / 100.0;
    }

    @Transactional
    public void updateCurrentPrices() {
        List<MemberStock> stocks = memberStockRepository.findAll();
        for (MemberStock stock : stocks) {
            Long currentPrice = koreaInvestmentApiService.getCurrentPrice(stock.getCode());
            stock.setAveragePrice(currentPrice);
            memberStockRepository.save(stock);
        }
    }
}