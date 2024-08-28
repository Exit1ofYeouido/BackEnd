package com.example.Home.MemberService;

import com.example.Home.Kis.KoreaInvestmentApiService;
import com.example.Home.Member.MemberStock;
import com.example.Home.MemberRepository.MemberStockRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class MemberStockService {

    private MemberStockRepository memberStockRepository;
    private KoreaInvestmentApiService koreaInvestmentApiService;

    public MemberStockService(MemberStockRepository memberStockRepository, KoreaInvestmentApiService koreaInvestmentApiService) {
        this.memberStockRepository = memberStockRepository;
        this.koreaInvestmentApiService = koreaInvestmentApiService;
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
