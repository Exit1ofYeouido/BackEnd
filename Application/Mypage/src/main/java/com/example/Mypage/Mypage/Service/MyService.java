package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Common.Entity.PopupCheck;
import com.example.Mypage.Common.Entity.StockTradeHistory;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Common.Repository.MemberRepository;
import com.example.Mypage.Common.Repository.MemberStockRepository;
import com.example.Mypage.Common.Repository.PopupCheckRepository;
import com.example.Mypage.Common.Repository.TradeRepository;
import com.example.Mypage.Mypage.Dto.Other.EarningRate;
import com.example.Mypage.Mypage.Dto.out.AllAssetDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MyService {


    private final MemberRepository memberRepository;
    private final MemberStockRepository memberStockRepository;
    private final ApiService apiService;
    private final PopupCheckRepository popupCheckRepository;
    private final AccountRepository accountRepository;
    private final TradeRepository tradeRepository;


    public GetAllMyPageResponseDto getAllMyPage(Long memId) {

        Account account = accountRepository.findByMemberId(memId).orElseThrow(
                () -> new AccountNotFoundException("계좌가 존재하지않습니다.")
        );
        List<MemberStock> memberStock = memberStockRepository.findByMemberId(memId);
        AllAssetDto AllAsset = CalcAllAssets(memberStock);
        List<EarningRate> earningRates = Top3EarningRateAssets(memberStock);

        return GetAllMyPageResponseDto.of(account.getPoint(), AllAsset.getCalcAssetsEarningRate(), earningRates,account.getAccountNumber(),AllAsset.getAllCost());
    }

    private AllAssetDto CalcAllAssets(List<MemberStock> memberStocks) {

        double allCost = 0;
        double currentAllCost = 0;

        
        if (memberStocks.isEmpty()) {
            return AllAssetDto.builder()
                    .calcAssetsEarningRate("0")
                    .allCost(0)
                    .build();
        }

        for (MemberStock memberStock : memberStocks) {
            double stockCount = memberStock.getAmount();
            double stockPrice = memberStock.getAveragePrice();

            double currentPrice = apiService.getPrice(memberStock.getStockCode());

            allCost = allCost + (stockPrice * stockCount);
            currentAllCost = currentAllCost + (currentPrice * stockCount);
        }


        if (allCost==currentAllCost){
            return AllAssetDto.builder()
                    .calcAssetsEarningRate("0")
                    .allCost((int) currentAllCost)
                    .build();
        }

        double value = (currentAllCost -allCost)/allCost * 100;
        DecimalFormat df = new DecimalFormat("#.##");

        return AllAssetDto.builder()
                .calcAssetsEarningRate(df.format(value))
                .allCost((int) currentAllCost)
                .build();
    }


    private List<EarningRate> Top3EarningRateAssets(List<MemberStock> memberStocks) {

        List<EarningRate> top3EarningRates = new ArrayList<>();

        for (MemberStock memberStock : memberStocks) {
            double stockCount = memberStock.getAmount();
            int stockPrice = memberStock.getAveragePrice();
            int currentPrice = apiService.getPrice(memberStock.getStockCode());

            double stock = stockCount * stockPrice;
            double currentStock = stockCount * currentPrice;
            String finalEarningRate ;

            if (currentStock==stock){
                finalEarningRate="0";
            }
            
            double value = (currentStock-stock)/stock * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            finalEarningRate = df.format(value);


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


    public void giveStock(GiveStockDto giveStockDto) {

        Member member = memberRepository.findById(giveStockDto.getMemId())
                .orElseThrow(() -> new MemberNotFoundException("주식을 증정할 유저를 찾을 수 없습니다." + giveStockDto.getMemId()));

        MemberStock memberStock = memberStockRepository.findByStockNameAndMember(giveStockDto.getEnterpriseName()
                , giveStockDto.getMemId());

        if (memberStock != null) {
            int avgPrice = (int) ((memberStock.getAmount() * memberStock.getAveragePrice() +
                    giveStockDto.getAmount() * giveStockDto.getPrice()) / (memberStock.getAmount()
                    + giveStockDto.getAmount()));

            double sumAvailableAmount = calcAvailableAmount(memberStock.getAvailableAmount(), giveStockDto.getAmount());

            memberStock.setAmount(memberStock.getAmount() + giveStockDto.getAmount());
            memberStock.setAvailableAmount(sumAvailableAmount);
            memberStock.setAveragePrice(avgPrice);
            memberStock.setUpdatedAt(LocalDateTime.now());
            memberStockRepository.save(memberStock);

        } else {
            memberStock = MemberStock.builder()
                    .member(member)
                    .stockName(giveStockDto.getEnterpriseName())
                    .amount(giveStockDto.getAmount())
                    .availableAmount(giveStockDto.getAmount())
                    .stockCode(giveStockDto.getCode())
                    .averagePrice(giveStockDto.getPrice())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            memberStockRepository.save(memberStock);
        }

        addStockTrade(giveStockDto, member, memberStock);
    }

    private double calcAvailableAmount(double originAmount, double newAmount) {
        double sumAmount = originAmount + newAmount;
        if (sumAmount >= 1) {
            return sumAmount - 1;
        }
        return sumAmount;
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
        StockTradeHistory stockTradeHistory = StockTradeHistory.builder()
                .stockName(giveStockDto.getEnterpriseName())
                .tradeType("in")
                .member(member)
                .amount(giveStockDto.getAmount())
                .createdAt(LocalDateTime.now())
                .memberStock(memberStock)
                .build();

        tradeRepository.save(stockTradeHistory);

    }
}
