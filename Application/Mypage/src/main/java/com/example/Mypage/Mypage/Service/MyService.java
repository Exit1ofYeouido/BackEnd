package com.example.Mypage.Mypage.Service;


import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Common.Entity.PopupCheck;
import com.example.Mypage.Common.Repository.MemberRepostory;
import com.example.Mypage.Common.Repository.MemberStockRepository;
import com.example.Mypage.Common.Repository.PopupCheckRepository;
import com.example.Mypage.Mypage.Dto.Other.EarningRate;
import com.example.Mypage.Mypage.Dto.out.GetAllMyPageResponseDto;
import com.example.Mypage.Mypage.Dto.out.GetTutorialCheckResponseDto;
import com.example.Mypage.Mypage.Kafka.Dto.GiveStockDto;
import com.example.Mypage.Mypage.Webclient.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyService {

    private final MemberRepostory memberRepostory;
    private final MemberStockRepository memberStockRepository;
    private final ApiService apiService;
    private final PopupCheckRepository popupCheckRepository;


    public GetAllMyPageResponseDto getAllMyPage(Long memId) {
        Optional<Member> member = memberRepostory.findById(memId);
        List<MemberStock> memberStock=memberStockRepository.findByMemberId(memId);
        String calcAssetsEarningRate=CalcAllAsssets(memberStock);
        List<EarningRate> earningRates=Top3EarningRateAssets(memberStock);
        int allCost=AllAssetsCount(memberStock);

        return GetAllMyPageResponseDto.of(member.get().getPoint(),calcAssetsEarningRate,earningRates,allCost);

    }

    private String CalcAllAsssets(List<MemberStock> memberStocks){
        double allCost = 0;
        double currentAllCost = 0;

        for (MemberStock memberStock: memberStocks){
            double stockcount=memberStock.getCount();
            double stockprice=memberStock.getAveragePrice();
            double currentprice=apiService.getPrise(memberStock.getStockCode());

            allCost = allCost + (stockprice * stockcount);
            currentAllCost = currentAllCost + (currentprice *stockcount);

        }


        if (allCost>currentAllCost){
            double value = (1 - (currentAllCost / allCost)) * 100;
            DecimalFormat df=new DecimalFormat("-#.##");
            return df.format(value);
        }
        double value = (1 - (allCost/currentAllCost)) * 100;
        DecimalFormat df=new DecimalFormat("#.##");
        return df.format(value);
    }

    private int AllAssetsCount(List<MemberStock> memberStocks){
        int allCost = 0;

        for (MemberStock memberStock: memberStocks){
            double stockcount=memberStock.getCount();
            int stockprice=memberStock.getAveragePrice();
            allCost = (int) (allCost + (stockprice * stockcount));


        }
        return allCost;
    }

    private List<EarningRate> Top3EarningRateAssets(List<MemberStock> memberStocks) {

        List<EarningRate> top3EarningRates = new ArrayList<>();
        for (MemberStock memberStock : memberStocks) {
            double stockCount = memberStock.getCount();
            int stockPrice = memberStock.getAveragePrice();
            int currentPrice = apiService.getPrise(memberStock.getStockCode());

            double stock = stockCount * stockPrice;
            double currentStock = stockCount * currentPrice;
            String finalEarningRate = null;

            if (stock > currentStock) {
                double value = (1 - (currentStock / stock)) * 100;
                DecimalFormat df=new DecimalFormat("-#.##");
                finalEarningRate=df.format(value);

            } else {
                double value = (1 - (stock/currentStock)) * 100;
                DecimalFormat df=new DecimalFormat("#.##");
                finalEarningRate=df.format(value);
            }

            EarningRate earningRate=EarningRate.builder()
                    .earningRate(finalEarningRate)
                    .enterpriseName(memberStock.getStockName())
                    .build();
            top3EarningRates.add(earningRate);
        }

        top3EarningRates=top3EarningRates.stream()
                .sorted(Comparator.comparing(EarningRate::getEarningRate).reversed())
                .limit(3)
                .collect(Collectors.toList());

        return top3EarningRates;

    }

    public void giveStock(GiveStockDto giveStockDto) {

        Optional<Member> member=memberRepostory.findById(giveStockDto.getMemId());
        MemberStock memberStock=memberStockRepository.findByStockNameAndMember(giveStockDto.getEnterpriseName()
                ,giveStockDto.getMemId());

        if (memberStock !=null){
            memberStock.setCount(memberStock.getCount()+giveStockDto.getAmount());
            memberStock.setUpdateAt(LocalDateTime.now());
            memberStockRepository.save(memberStock);
        }else {
            MemberStock new_memberStock = MemberStock.builder()
                    .member(member.get())
                    .stockName(giveStockDto.getEnterpriseName())
                    .count(giveStockDto.getAmount())
                    .stockCode(giveStockDto.getCode())
                    .averagePrice(giveStockDto.getPrice())
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .build();
            memberStockRepository.save(new_memberStock);
        }



    }


    public GetTutorialCheckResponseDto getTutorialCheck(String type, Long memId) {

        PopupCheck popupCheck=popupCheckRepository.findByPopupTypeAndMemberId(type,memId);

        if (popupCheck !=null) {
            return GetTutorialCheckResponseDto.of(true);
        }
        return GetTutorialCheckResponseDto.of(false);
    }

    public void saveTutorialCheck(String type, Long memId) {

        PopupCheck popupCheck=PopupCheck
                .builder()
                .popupType(type)
                .memberId(memId)
                .build();
        popupCheckRepository.save(popupCheck);

    }
}
