package com.example.Home.HomeService;

import com.example.Home.Common.Entity.Account;
import com.example.Home.Common.Entity.Member;
import com.example.Home.Common.Entity.MemberStock;
import com.example.Home.Common.Repository.AccountRepository;
import com.example.Home.Common.Repository.MemberRepository;
import com.example.Home.Common.Repository.MemberStockRepository;
import com.example.Home.HomeDTO.HomeResponseDTO;
import com.example.Home.Kis.KoreaInvestmentApiService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HomeService {

    private final MemberRepository memberRepository;
    private final MemberStockRepository memberStockRepository;
    private final AccountRepository accountRepository;
    private final KoreaInvestmentApiService koreaInvestmentApiService;

    public HomeService(MemberRepository memberRepository, MemberStockRepository memberStockRepository,
                       AccountRepository accountRepository, KoreaInvestmentApiService koreaInvestmentApiService) {
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

        double totalStockValue = 0;

        for (MemberStock memberStock : memberStocks) {
            double stockAmount = memberStock.getAmount();
            double stockPrice = stockPrices.getOrDefault(memberStock.getCode(), 0L);
            totalStockValue += stockAmount * stockPrice;
        }

        return (int) totalStockValue;
    }

    private Map<String, Long> getStockPrices(List<String> stockCodes) {
        Map<String, Long> stockPrices = new HashMap<>();

        Set<String> uniqueCodes = new HashSet<>(stockCodes);
        int idx = 0;

        for (String code : uniqueCodes) {
            Long price = 0L;

            if (idx++ % 2 == 0) {
                price = koreaInvestmentApiService.getCurrentAPrice(code);
            } else {
                price = koreaInvestmentApiService.getCurrentBPrice(code);
            }

            stockPrices.put(code, price);
        }

        log.debug("getStockPrices result: {}", stockPrices);
        return stockPrices;
    }

    private double calculateEarningRate(List<MemberStock> memberStocks) {
        if (memberStocks.isEmpty()) {
            return 0.0;
        }

        Map<String, Long> currentPrices = getStockPrices(
                memberStocks.stream().map(MemberStock::getCode).collect(Collectors.toList()));
        System.out.println("current prices" + currentPrices);

        double totalCurrentValue = 0.0;
        double totalPurchaseValue = 0.0;

        for (MemberStock memberStock : memberStocks) {
            long currentPrice = currentPrices.getOrDefault(memberStock.getCode(), 0L);
            long purchasePrice = memberStock.getAveragePrice();
            double currentValue = memberStock.getAmount() * currentPrice;
            double purchaseValue = memberStock.getAmount() * purchasePrice;

            totalCurrentValue += currentValue;
            totalPurchaseValue += purchaseValue;
        }
        double earningRate = ((totalCurrentValue - totalPurchaseValue) / totalPurchaseValue) * 100;

        return Math.round(earningRate * 100.0) / 100.0;
    }

}