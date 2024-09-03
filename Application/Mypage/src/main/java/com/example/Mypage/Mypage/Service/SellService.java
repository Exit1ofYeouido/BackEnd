package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Entity.MemberStock;
import com.example.Mypage.Common.Entity.StockSellRequestA;
import com.example.Mypage.Common.Entity.StockSellRequestB;
import com.example.Mypage.Common.Repository.MemberStockRepository;
import com.example.Mypage.Common.Repository.StockSaleRequestARepository;
import com.example.Mypage.Common.Repository.StockSaleRequestBRepository;
import com.example.Mypage.Common.Repository.TradeRepository;
import com.example.Mypage.Mypage.Dto.in.StockSellRequestDto;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellService {

    private final StockSaleRequestARepository stockSaleRequestARepository;
    private final StockSaleRequestBRepository stockSaleRequestBRepository;
    private final MemberStockRepository memberStockRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public boolean saveStockSellRequest(Long memberId, StockSellRequestDto stockSellRequestDto) {

        String enterpriseName = stockSellRequestDto.getStockName();

        MemberStock memberStock = memberStockRepository.findByStockNameAndMember(enterpriseName, memberId);
        Member member = memberStock.getMember();

        if (memberStock == null) {
            throw new NoSuchElementException("보유한 주식만 판매가능합니다.");
        }

        double canSellAmount = memberStock.getAvailableAmount() - stockSellRequestDto.getSellAmount();
        if (canSellAmount < 0) {
            throw new IllegalStateException("보유한 주식을 초과하여 판매할 수 없습니다.");
        }

        saveSellRequest(stockSellRequestDto, member);
        return true;
    }

    private void saveSellRequest(StockSellRequestDto stockSellRequestDto, Member member) {
        LocalTime now = LocalTime.now();

        if (now.isAfter(LocalTime.of(9, 30)) && now.isBefore(LocalTime.of(14, 30))) {
            StockSellRequestA saveStockSellRequestA = stockSellRequestDto.toSellRequestA(member);
            stockSaleRequestARepository.save(saveStockSellRequestA);
            log.info("StockSaleRequestA 저장 성공 => {}", saveStockSellRequestA);
        } else {
            stockSaleRequestBRepository.save(stockSellRequestDto.toSellRequestB(member));
            StockSellRequestB saveStockSellRequestB = stockSellRequestDto.toSellRequestB(member);
            log.info("StockSaleRequestB 저장 성공 => {}", saveStockSellRequestB);
        }
    }
}
