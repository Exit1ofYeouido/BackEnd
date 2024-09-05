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

    public void gethistoryStock(String code, Integer year, Integer month) {

        List<GetDate> searchLogs=searchLogRepository.findByYearAndMonth(code,year,month);
        List<GetDate> getstockLogs=searchLogRepository.findByGetStockLogs(code,year,month);
        List<GetDate> getNotStockLogs=searchLogRepository.findByNotGetStockLogs(code,year,month);

        List<GetHistoryStockResponseDto> getHistoryStockResponseDtos =new ArrayList<>();

        HashMap<String,Integer> search=putsum(searchLogs);
        HashMap<String,Integer> getStock=putsum(getstockLogs);
        HashMap<String, Integer> getNotStock=putsum(getNotStockLogs);



    }

    private HashMap<String,Integer> putsum(List<GetDate> getDates){
        HashMap<String,Integer>  temp=new HashMap<>();
        for (GetDate getDate :getDates){
            String date=getDate.getStartTime();
            temp.put(date,temp.get(date)+1);
        }

        return temp;
    }
}
