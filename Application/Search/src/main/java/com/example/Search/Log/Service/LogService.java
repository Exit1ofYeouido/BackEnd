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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public List<GetHistoryStockResponseDto> gethistoryStock(String enterpriseName, String year, String month) throws ParseException {

        if (month.equals("0")){
            List<SearchLog> allyearsearch=searchLogRepository.findByYear(enterpriseName,year);
            List<SearchLog> allgetStockLogs=searchLogRepository.findBygetAllStockLogs(enterpriseName,year);
            HashMap<String,Integer> search=getAllDate(allyearsearch);
            HashMap<String,Integer> getStock=getAllDate(allgetStockLogs);
            Map<String, Integer> sortedsearch = new TreeMap<>(search);
            List<GetHistoryStockResponseDto> getHistoryStockResponseDtos=getFinalDto(sortedsearch,search,getStock);

            return getHistoryStockResponseDtos;
        }

        List<SearchLog> searchLogs=searchLogRepository.findByYearAndMonth(enterpriseName,year,month);
        List<SearchLog> getstockLogs=searchLogRepository.findByGetStockLogs(enterpriseName,year,month);


        HashMap<String,Integer> search=getDate(searchLogs);
        HashMap<String,Integer> getStock=getDate(getstockLogs);
        Map<String, Integer> sortedsearch = new TreeMap<>(search);

        List<GetHistoryStockResponseDto> getHistoryStockResponseDtos=getFinalDto(sortedsearch,search,getStock);



        return getHistoryStockResponseDtos;
    }

    private HashMap<String,Integer> getDate(List<SearchLog> getDates) throws ParseException {
        HashMap<String,Integer>  temp=new HashMap<>();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat( "yyyy-MM-dd");

        for (SearchLog searchLog :getDates){
            Date formatDate = sdf.parse(searchLog.getSearchTime());
            String date= simpleDateFormat.format(formatDate);
            temp.put(date,temp.getOrDefault(date,0)+1);
        }

        return temp;
    }

    private HashMap<String,Integer> getAllDate(List<SearchLog> getDates) throws ParseException {
        HashMap<String,Integer>  temp=new HashMap<>();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat( "yyyy-MM");

        for (SearchLog searchLog :getDates){
            Date formatDate = sdf.parse(searchLog.getSearchTime());
            String date= simpleDateFormat.format(formatDate);
            temp.put(date,temp.getOrDefault(date,0)+1);
        }

        return temp;
    }

    private List<GetHistoryStockResponseDto> getFinalDto (Map<String, Integer> sortedsearch,HashMap<String,Integer> search,HashMap<String,Integer> getStock){

        List<GetHistoryStockResponseDto> getHistoryStockResponseDtos =new ArrayList<>();
        for (String date:sortedsearch.keySet()){

            GetHistoryStockResponseDto getHistoryStockResponseDto=new GetHistoryStockResponseDto();
            Integer getSearchValue=search.get(date);


            Integer getStockValue= getStock.get(date);
            if (getStockValue ==null){
                getStockValue=0;
            }

            GetHistoryStockResponseDto getHistoryStockResponseDto1=getHistoryStockResponseDto.builder()
                    .date(date)
                    .totalCount(getSearchValue)
                    .holdingCount(getStockValue)
                    .notHoldingCount(getSearchValue-getStockValue)
                    .build();

            getHistoryStockResponseDtos.add(getHistoryStockResponseDto1);
        }
        return getHistoryStockResponseDtos;
    }

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

    public List<GetSearchLogMemberDto> getLogMember(Long memberId, int year, int month) {
        List<GetSearchLogMemberDto> getSearchLogMemberDtos = searchLogRepository.findByMemberIdWithYearAndMonth(memberId, year, month);
        for(GetSearchLogMemberDto getSearchLogMemberDto : getSearchLogMemberDtos) {
            String stockCode = stockRepository.findByName(getSearchLogMemberDto.getEnterpriseName()).getCode();
            Optional<MemberStockHolding> memberStockHolding = memberStockHoldingRepository.findByMemberIdAndStockCode(memberId, stockCode);
            getSearchLogMemberDto.setHolding(memberStockHolding.isPresent());
        }
        return getSearchLogMemberDtos;
    }
}
