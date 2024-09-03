package com.example.Mypage.Mypage.Service;


import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Common.Entity.PopupCheck;
import com.example.Mypage.Common.Entity.Trade;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Common.Repository.MemberRepository;
import com.example.Mypage.Common.Repository.MemberStockRepository;
import com.example.Mypage.Common.Repository.PopupCheckRepository;
import com.example.Mypage.Common.Repository.TradeRepository;
import com.example.Mypage.Mypage.Dto.Other.EarningRate;
import com.example.Mypage.Mypage.Dto.out.GetAllMyPageResponseDto;
import com.example.Mypage.Mypage.Dto.out.GetTutorialCheckResponseDto;
import com.example.Mypage.Mypage.Exception.AccountNotFoundException;
import com.example.Mypage.Mypage.Exception.MemberNotFoundException;
import com.example.Mypage.Mypage.Kafka.Dto.GiveStockDto;
import com.example.Mypage.Mypage.Webclient.Service.ApiService;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyService {

    private static final Logger log = LoggerFactory.getLogger(MyService.class);
    private MemberRepository memberRepository;
    private final MemberStockRepository memberStockRepository;
    private final ApiService apiService;
    private final PopupCheckRepository popupCheckRepository;
    private final AccountRepository accountRepository;
    private final TradeRepository tradeRepository;

    @Transactional(readOnly = true)
    public GetAllMyPageResponseDto getAllMyPage(Long memId) {

        Account account = accountRepository.findByMemberId(memId).orElseThrow(
                ()-> new AccountNotFoundException("계좌가 존재하지않습니다.")
        );
        List<MemberStock> memberStock = memberStockRepository.findByMemberId(memId);
        String calcAssetsEarningRate = CalcAllAssets(memberStock);
        List<EarningRate> earningRates = Top3EarningRateAssets(memberStock);
        int allCost = AllAssetsCount(memberStock);

        return GetAllMyPageResponseDto.of(account.getPoint(), calcAssetsEarningRate, earningRates, allCost);

    }

    private String CalcAllAssets(List<MemberStock> memberStocks) {

        double allCost = 0;
        double currentAllCost = 0;

        for (MemberStock memberStock : memberStocks) {
            double stockcount = memberStock.getCount();
            double stockprice = memberStock.getAveragePrice();
            double currentprice = apiService.getPrice(memberStock.getStockCode());

            allCost = allCost + (stockprice * stockcount);
            currentAllCost = currentAllCost + (currentprice * stockcount);

        }

        if (allCost > currentAllCost) {
            double value = (1 - (currentAllCost / allCost)) * 100;
            DecimalFormat df = new DecimalFormat("-#.##");
            return df.format(value);
        }
        double value = (1 - (allCost / currentAllCost)) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    private int AllAssetsCount(List<MemberStock> memberStocks) {
        int allCost = 0;

        for (MemberStock memberStock : memberStocks) {
            double stockcount = memberStock.getCount();
            int stockprice = memberStock.getAveragePrice();
            allCost = (int) (allCost + (stockprice * stockcount));


        }
        return allCost;
    }

    private List<EarningRate> Top3EarningRateAssets(List<MemberStock> memberStocks) {

        List<EarningRate> top3EarningRates = new ArrayList<>();

        for (MemberStock memberStock : memberStocks) {
            double stockCount = memberStock.getCount();
            int stockPrice = memberStock.getAveragePrice();
            int currentPrice = apiService.getPrice(memberStock.getStockCode());

            double stock = stockCount * stockPrice;
            double currentStock = stockCount * currentPrice;
            String finalEarningRate = null;

            if (stock > currentStock) {
                double value = (1 - (currentStock / stock)) * 100;
                DecimalFormat df = new DecimalFormat("-#.##");
                finalEarningRate = df.format(value);

            } else {
                double value = (1 - (stock / currentStock)) * 100;
                DecimalFormat df = new DecimalFormat("#.##");
                finalEarningRate = df.format(value);
            }

            EarningRate earningRate = EarningRate.builder()
                    .earningRate(finalEarningRate)
                    .enterpriseName(memberStock.getStockName())
                    .build();
            top3EarningRates.add(earningRate);
        }

        top3EarningRates = top3EarningRates
                .stream()
                .sorted(Comparator.comparing(EarningRate::getEarningRate).reversed())
                .limit(3)
                .collect(Collectors.toList());

        return top3EarningRates;

    }

    @Transactional
    public void giveStock(GiveStockDto giveStockDto) {


        Member member = memberRepository.findById(giveStockDto.getMemId())
                .orElseThrow(() -> new MemberNotFoundException("주식을 증정할 유저를 찾을 수 없습니다." + giveStockDto.getMemId()));

        MemberStock memberStock = memberStockRepository.findByStockNameAndMember(giveStockDto.getEnterpriseName()
                , giveStockDto.getMemId());

        if (memberStock != null) {
            int avgPrice = (int) ((memberStock.getCount() * memberStock.getAveragePrice() +
                    giveStockDto.getAmount() * giveStockDto.getPrice()) / (memberStock.getCount()
                    + giveStockDto.getAmount()));

            memberStock.setCount(memberStock.getCount() + giveStockDto.getAmount());
            memberStock.setAveragePrice(avgPrice);
            memberStock.setUpdatedAt(LocalDateTime.now());
            memberStockRepository.save(memberStock);

        } else {
            MemberStock new_memberStock = MemberStock.builder()
                    .member(member)
                    .stockName(giveStockDto.getEnterpriseName())
                    .count(giveStockDto.getAmount())
                    .stockCode(giveStockDto.getCode())
                    .averagePrice(giveStockDto.getPrice())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            memberStockRepository.save(new_memberStock);
        }
        addStockTrade(giveStockDto, member, memberStock);
    }


    @Transactional(readOnly = true)
    public GetTutorialCheckResponseDto getTutorialCheck(String type, Long memId) {

        PopupCheck popupCheck = popupCheckRepository.findByPopupTypeAndMemberId(type, memId);

        if (popupCheck != null) {
            return GetTutorialCheckResponseDto.of(true);
        }
        return GetTutorialCheckResponseDto.of(false);
    }

    public void saveTutorialCheck(String type, Long memId) {

        PopupCheck popupCheck = PopupCheck
                .builder()
                .popupType(type)
                .memberId(memId)
                .build();
        popupCheckRepository.save(popupCheck);

    }



    @Transactional(readOnly = true)
    public List<MemberStock> getAllStock(Long memId) {
        List<MemberStock> memberStocks = memberStockRepository.findByMemberId(memId);
        return memberStocks;
    }

    // 주식 거래내역 추가
    private void addStockTrade(GiveStockDto giveStockDto, Member member, MemberStock memberStock) {

        Trade trade = Trade.builder()
                .stockName(giveStockDto.getEnterpriseName())
                .tradeType("입금")
                .member(member)
                .count(giveStockDto.getAmount())
                .createdAt(LocalDateTime.now())
                .memberStock(memberStock)
                .build();


        tradeRepository.save(trade);
        log.info("주식 거래내역 저장 성공 => {}", trade.getId());
    }

  
}
