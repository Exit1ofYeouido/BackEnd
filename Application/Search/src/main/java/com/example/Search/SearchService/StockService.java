package com.example.Search.SearchService;

import com.example.Search.Api.KisService;
import com.example.Search.SearchDTO.DailyStockPriceDTO;
import com.example.Search.SearchDTO.StockDetailDTO;
import com.example.Search.SearchDTO.StocksDTO;
import com.example.Search.SearchRepository.MemberStockRepository.MemberStock;
import com.example.Search.SearchRepository.MemberStockRepository.MemberStockRepository;
import com.example.Search.SearchRepository.StockRepository.Stock;
import com.example.Search.SearchRepository.StockRepository.StockRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final KisService kisService;
    private final MemberStockRepository memberStockRepository;


    public StockService(StockRepository stockRepository, KisService kisService, MemberStockRepository memberStockRepository) {
        this.stockRepository = stockRepository;
        this.kisService = kisService;
        this.memberStockRepository = memberStockRepository;
    }

    public List<StocksDTO> getStocks(Long memberId) {
        List<StocksDTO> result = new ArrayList<>();

        // 1. Get top 5 stocks from member_stock table for the given memberId
        List<MemberStock> memberStocks = memberStockRepository.findTop5ByMemberIdOrderByCountDesc(memberId, PageRequest.of(0, 5));
        List<String> memberStockCodes = memberStocks.stream()
                .map(MemberStock::getStockCode)
                .collect(Collectors.toList());

        for (String code : memberStockCodes) {
            Optional<Stock> stockOptional = stockRepository.findById(code);
            if (stockOptional.isPresent()) {
                result.add(createStocksDTO(stockOptional.get()));
            }
        }

        // 2. Get random stocks to fill up to 10 total
        int remainingCount = 10 - result.size();
        if (remainingCount > 0) {
            List<Stock> randomStocks = stockRepository.findRandomStocksExcluding(memberStockCodes, remainingCount);
            for (Stock stock : randomStocks) {
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
    public StockDetailDTO getStockByCode(String code, String period,Long memberId) {
        Optional<Stock> stockOptional = stockRepository.findById(code);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            Long currentPrice = kisService.getCurrentPrice(stock.getCode());
            String previousPrice = kisService.getPreviousPrice(stock.getCode());
            String previousRate = kisService.getPreviousRate(stock.getCode());
            List<DailyStockPriceDTO> stockPriceList = kisService.getStockPriceList(stock.getCode(), period);

            Double stockCount = memberStockRepository.findStockCountByMemberIdAndStockCode(memberId, stock.getCode()).orElse(0.0);

            return new StockDetailDTO(stock.getName(), stock.getCode(), currentPrice.intValue(),stockCount, previousPrice, previousRate, stockPriceList);
        } else {
            return null;
        }
    }

    public List<StocksDTO> searchSimilarStocks(String keyword) {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Stock> similarStocks = stockRepository.findSimilarStocks(keyword, pageRequest);

        return similarStocks.stream()
                .map(this::createStocksDTO)
                .collect(Collectors.toList());
    }
}

