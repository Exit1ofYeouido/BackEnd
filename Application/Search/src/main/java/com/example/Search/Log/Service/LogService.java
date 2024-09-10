package com.example.Search.Log.Service;


import com.example.Search.Common.Entity.MemberStockHolding;
import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Common.Entity.Stock;
import com.example.Search.Common.Repository.MemberStockHoldingRepository;
import com.example.Search.Common.Repository.SearchLogRepository;
import com.example.Search.Common.Repository.StockRepository;
import com.example.Search.Exception.NotAdminException;
import com.example.Search.Log.Dto.out.GetSearchLogMemberStockDto;
import com.example.Search.Log.Dto.out.MemberCountDto;
import com.example.Search.Log.Dto.out.MemberStockCountByYearDto;
import com.example.Search.Log.Dto.out.MemberStockCountDto;
import com.example.Search.Log.Dto.out.StockCountByYearDto;
import com.example.Search.Log.Dto.out.StockCountDto;
import com.example.Search.Log.Dto.out.StockCountResponseDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    @Value("${auth.admin}")
    private String ADMIN;

    private final SearchLogRepository searchLogRepository;
    private final MemberStockHoldingRepository memberStockHoldingRepository;
    private final StockRepository stockRepository;

    public void recordSearchLog(Long memberId, String stockCode) {
        String enterpriseName = stockRepository.findByCode(stockCode).getName();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String searchTime = now.format(formatter);
        System.out.println(searchTime);
        Optional<MemberStockHolding> holding = memberStockHoldingRepository.findByMemberIdAndStockCode(memberId,
                stockCode);
        Boolean isHold = holding.isPresent();
        SearchLog searchLog = new SearchLog(memberId, enterpriseName, searchTime, isHold);
        searchLogRepository.save(searchLog);
    }

    public GetSearchLogMemberStockDto getLogMemberStock(String role, Long memberId, String enterpriseName, int year,
                                                        int month) {
        isAdmin(role);
        String stockCode = stockRepository.findByName(enterpriseName).getCode();
        Optional<MemberStockHolding> memberStockHolding = memberStockHoldingRepository.findByMemberIdAndStockCode(
                memberId, stockCode);
        boolean isHolding = memberStockHolding.isPresent();

        if (month == 0) {
            List<MemberStockCountByYearDto> memberStockCountByYearDtos = searchLogRepository.countByMemberIdAndEnterpriseNameWithYear(
                    memberId, enterpriseName, year);
            return GetSearchLogMemberStockDto.builder()
                    .isHolding(isHolding)
                    .countResult(memberStockCountByYearDtos)
                    .build();
        } else {
            List<MemberStockCountDto> memberStockCountDtos = searchLogRepository.countByMemberIdAndEnterpriseNameWithYearAndMonth(
                    memberId, enterpriseName, year, month);
            return GetSearchLogMemberStockDto.builder()
                    .isHolding(isHolding)
                    .countResult(memberStockCountDtos)
                    .build();
        }

    }


    public List<MemberCountDto> getLogMember(String role, Long memberId, int year, int month) {
        isAdmin(role);
        List<MemberCountDto> memberCountDtos;
        if (month == 0) {
            memberCountDtos = searchLogRepository.findByMemberIdWithYear(memberId, year);
        } else {
            memberCountDtos = searchLogRepository.findByMemberIdWithYearAndMonth(memberId, year, month);
        }
        for (MemberCountDto memberCountDto : memberCountDtos) {
            String stockCode = stockRepository.findByName(memberCountDto.getEnterpriseName()).getCode();
            Optional<MemberStockHolding> memberStockHolding = memberStockHoldingRepository.findByMemberIdAndStockCode(
                    memberId, stockCode);
            memberCountDto.setHolding(memberStockHolding.isPresent());
        }
        return memberCountDtos;

    }

    public StockCountResponseDto getLogStock(String role, String enterpriseInfo, int year,
                                             int month) {
        isAdmin(role);

        Stock stock = stockRepository.findByCode(enterpriseInfo);
        
        if (stock != null) {
            enterpriseInfo = stock.getName();
        }

        if (month == 0) {
            List<StockCountByYearDto> stockCountByYearDtos = searchLogRepository.findByEnterpriseNameWithYear(
                    enterpriseInfo, year);
            return StockCountResponseDto.builder()
                    .countResults(stockCountByYearDtos)
                    .build();
        } else {
            List<StockCountDto> stockCountDtos = searchLogRepository.findByEnterpriseNameWithYearAndMonth(
                    enterpriseInfo, year, month);
            return StockCountResponseDto.builder()
                    .countResults(stockCountDtos)
                    .build();
        }
    }

    public void isAdmin(String role) {
        if (!role.equals(ADMIN)) {
            throw new NotAdminException("인증된 사용자만 접근 가능합니다.");
        }
    }

}
