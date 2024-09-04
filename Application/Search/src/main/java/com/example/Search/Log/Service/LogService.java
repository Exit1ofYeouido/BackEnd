package com.example.Search.Log.Service;


import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Common.Repository.MemberStockHoldingRepository;
import com.example.Search.Common.Repository.SearchLogRepository;
import com.example.Search.Log.Dto.out.GetHistoryStockResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class LogService {


    private final SearchLogRepository searchLogRepository;
    private final MemberStockHoldingRepository memberStockHoldingRepository;

    public void gethistoryStock(String code, Integer year, Integer month) {

        List<SearchLog> searchLogs=memberStockHoldingRepository.findByYearAndMonth(code,year,month);



        for (SearchLog searchLog:searchLogs){
            
        }



    }
}
