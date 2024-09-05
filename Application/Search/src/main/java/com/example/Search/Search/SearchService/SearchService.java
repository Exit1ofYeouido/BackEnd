package com.example.Search.Search.SearchService;


import com.example.Search.Search.Api.KisService;
import com.example.Search.Search.SearchDTO.DailyStockPriceDTO;
import com.example.Search.Search.SearchDTO.StockDetailDTO;
import com.example.Search.Search.SearchDTO.StockPriceListDTO;
import com.example.Search.Search.SearchDTO.StocksDTO;
import com.example.Search.Common.Entity.MemberStock;
import com.example.Search.Common.Repository.MemberStockRepository;
import com.example.Search.Common.Entity.Stock;
import com.example.Search.Common.Repository.StockRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final StockRepository stockRepository;
    private final KisService kisService;
    private final MemberStockRepository memberStockRepository;


    public SearchService(StockRepository stockRepository, KisService kisService, MemberStockRepository memberStockRepository) {
        this.stockRepository = stockRepository;
        this.kisService = kisService;
        this.memberStockRepository = memberStockRepository;
    }

    public List<StocksDTO> getStocks(Long memberId) {
        List<StocksDTO> result = new ArrayList<>();

        List<MemberStock> memberStocks = memberStockRepository.findTop5ByMemberIdOrderByCountDesc(memberId, PageRequest.of(0, 5));
        List<String> memberStockCodes = new ArrayList<>();

        for (MemberStock memberStock : memberStocks) {
            String code = memberStock.getStockCode();
            memberStockCodes.add(code);
            Optional<Stock> stockOptional = stockRepository.findById(code);
            if (stockOptional.isPresent()) {
                result.add(createStocksDTO(stockOptional.get()));
            }
        }

        int targetCount = memberStocks.isEmpty() ? 5 : 10;
        int remainingCount = targetCount - result.size();

        if (remainingCount > 0) {
            List<Stock> randomStocks;
            if (memberStockCodes.isEmpty()) {

                randomStocks = stockRepository.findRandomStocks(PageRequest.of(0, remainingCount));
            } else {

                randomStocks = stockRepository.findRandomStocksExcluding(memberStockCodes, remainingCount);
            }
            for (Stock stock : randomStocks) {
                result.add(createStocksDTO(stock));
            }
        }

        if (result.isEmpty()) {
            List<Stock> anyStocks = stockRepository.findRandomStocks(PageRequest.of(0, 5));
            for (Stock stock : anyStocks) {
                result.add(createStocksDTO(stock));
            }
        }

        return result;
    }
    private StocksDTO createStocksDTO (Stock stock){
        Long currentPrice = kisService.getCurrentPrice(stock.getCode());
        String previousPrice = kisService.getPreviousPrice(stock.getCode());
        String previousRate = kisService.getPreviousRate(stock.getCode());
        return new StocksDTO(stock.getName(), stock.getCode(), currentPrice.intValue(), previousPrice, previousRate);
    }
    public StockDetailDTO getStockByCode(String code, Long memberId) {
        Optional<Stock> stockOptional = stockRepository.findById(code);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            Long currentPrice = kisService.getCurrentPrice(stock.getCode());
            String previousPrice = kisService.getPreviousPrice(stock.getCode());
            String previousRate = kisService.getPreviousRate(stock.getCode());

            Double availableAmount = memberStockRepository.findAvailableCountByMemberIdAndStockCode(memberId, stock.getCode()).orElse(0.0);
            return new StockDetailDTO(stock.getName(), stock.getCode(), currentPrice.intValue(), availableAmount, previousPrice, previousRate);
        } else {
            return null;
        }
    }

    public StockPriceListDTO getStockPriceList(String code, String period) {
        List<DailyStockPriceDTO> stockPriceList = kisService.getStockPriceList(code, period);
        return new StockPriceListDTO(stockPriceList);
    }

    public List<StocksDTO> searchSimilarStocks(String keyword) {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Stock> similarStocks = stockRepository.findSimilarStocks(keyword, pageRequest);

        return similarStocks.stream()
                .map(this::createStocksDTO)
                .collect(Collectors.toList());
    }
}

