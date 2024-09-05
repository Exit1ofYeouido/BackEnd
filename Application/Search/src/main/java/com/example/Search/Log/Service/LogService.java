package com.example.Search.Log.Service;


import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Common.Repository.MemberStockHoldingRepository;
import com.example.Search.Common.Repository.SearchLogRepository;
import com.example.Search.Log.Dto.out.GetDate;
import com.example.Search.Log.Dto.out.GetHistoryStockResponseDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class LogService {


    private final SearchLogRepository searchLogRepository;
    private final MemberStockHoldingRepository memberStockHoldingRepository;

    public List<GetHistoryStockResponseDto> gethistoryStock(String code, Integer year, Integer month) {

        List<GetDate> searchLogs=searchLogRepository.findByYearAndMonth(code,year,month);
        List<GetDate> getstockLogs=searchLogRepository.findByGetStockLogs(code,year,month);

        List<GetHistoryStockResponseDto> getHistoryStockResponseDtos =new ArrayList<>();

        HashMap<String,Integer> search=getDate(searchLogs);
        HashMap<String,Integer> getStock=getDate(getstockLogs);

        for (String string:search.keySet()){
            GetHistoryStockResponseDto getHistoryStockResponseDto=new GetHistoryStockResponseDto();
            Integer getSearchValue=search.get(string);
            Integer getStockValue=getStock.get(string);
            getHistoryStockResponseDto.builder()
                    .date(string)
                    .totalCount(getSearchValue)
                    .holdingCount(getStockValue)
                    .notHoldingCount(getSearchValue-getStockValue);
            getHistoryStockResponseDtos.add(getHistoryStockResponseDto);
        }

        return getHistoryStockResponseDtos;
    }

    private HashMap<String,Integer> getDate(List<GetDate> getDates){
        HashMap<String,Integer>  temp=new HashMap<>();
        for (GetDate getDate :getDates){
            String date=format(getDate.getsearchTime(),"yyyy-MM-dd");
            temp.put(date,temp.get(date)+1);
        }

        return temp;
    }
}
