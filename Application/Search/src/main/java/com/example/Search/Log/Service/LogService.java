package com.example.Search.Log.Service;


import com.example.Search.Common.Entity.MemberStockHolding;
import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Common.Repository.MemberStockHoldingRepository;
import com.example.Search.Common.Repository.SearchLogRepository;
import com.example.Search.Common.Repository.StockRepository;
import com.example.Search.Log.Dto.out.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final SearchLogRepository searchLogRepository;
    private final MemberStockHoldingRepository memberStockHoldingRepository;
    private final StockRepository stockRepository;

    public void recordSearchLog(Long memberId, String stockCode) {
        String enterpriseName = stockRepository.findByCode(stockCode).getName();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String searchTime = now.format(formatter);
        System.out.println(searchTime);
        Optional<MemberStockHolding> holding = memberStockHoldingRepository.findByMemberIdAndStockCode(memberId, stockCode);
        Boolean isHold = holding.isPresent();
        SearchLog searchLog = new SearchLog(memberId, enterpriseName, searchTime, isHold);
        searchLogRepository.save(searchLog);
    }

    public GetSearchLogMemberStockDto getLogMemberStock(Long memberId, String enterpriseName, int year, int month) {
        String stockCode = stockRepository.findByName(enterpriseName).getCode();
        Optional<MemberStockHolding> memberStockHolding = memberStockHoldingRepository.findByMemberIdAndStockCode(memberId, stockCode);
        boolean isHolding = memberStockHolding.isPresent();

        if(month==0) {
            List<MemberStockCountByYearDto> memberStockCountByYearDtos = searchLogRepository.countByMemberIdAndEnterpriseNameWithYear(memberId, enterpriseName, year);
            return GetSearchLogMemberStockDto.builder()
                    .isHolding(isHolding)
                    .countResult(memberStockCountByYearDtos)
                    .build();
        }
        else {
            List<MemberStockCountDto> memberStockCountDtos = searchLogRepository.countByMemberIdAndEnterpriseNameWithYearAndMonth(memberId, enterpriseName, year, month);
            return GetSearchLogMemberStockDto.builder()
                    .isHolding(isHolding)
                    .countResult(memberStockCountDtos)
                    .build();
        }

    }

    public List<MemberCountDto> getLogMember(Long memberId, int year, int month) {
        List<MemberCountDto> memberCountDtos;
        if(month==0) {
            memberCountDtos = searchLogRepository.findByMemberIdWithYear(memberId, year);
        }
        else {
            memberCountDtos = searchLogRepository.findByMemberIdWithYearAndMonth(memberId, year, month);
        }
        for (MemberCountDto memberCountDto : memberCountDtos) {
            String stockCode = stockRepository.findByName(memberCountDto.getEnterpriseName()).getCode();
            Optional<MemberStockHolding> memberStockHolding = memberStockHoldingRepository.findByMemberIdAndStockCode(memberId, stockCode);
            memberCountDto.setHolding(memberStockHolding.isPresent());
        }
        return memberCountDtos;

    }

    public StockCountResponseDto getLogStock(String enterpriseName, int year, int month) {
        if (month == 0) {
            List<StockCountByYearDto> stockCountByYearDtos = searchLogRepository.findByEnterpriseNameWithYear(enterpriseName, year);
            return StockCountResponseDto.builder()
                    .countResults(stockCountByYearDtos)
                    .build();
        } else {
            List<StockCountDto> stockCountDtos = searchLogRepository.findByEnterpriseNameWithYearAndMonth(enterpriseName, year, month);
            return StockCountResponseDto.builder()
                    .countResults(stockCountDtos)
                    .build();
        }
    }
}
