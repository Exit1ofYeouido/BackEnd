package com.example.Search.Log.Service;


import com.example.Search.Common.Entity.SearchLog;
import com.example.Search.Common.Repository.MemberStockHoldingRepository;
import com.example.Search.Common.Repository.SearchLogRepository;
import com.example.Search.Log.Dto.out.GetDate;
import com.example.Search.Log.Dto.out.GetHistoryStockResponseDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final SearchLogRepository searchLogRepository;

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
}
