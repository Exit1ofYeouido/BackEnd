package com.example.Search.SearchController;


import com.example.Search.Api.KisService;
import com.example.Search.SearchDTO.StockDetailDTO;
import com.example.Search.SearchDTO.StocksDTO;
import com.example.Search.SearchService.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StockController {

    private final StockService stockService;
    private final KisService kisService;

    public StockController(StockService stockService, KisService kisService) {
        this.stockService = stockService;
        this.kisService = kisService;
    }

    @GetMapping("/search/stocks")
    public List<StocksDTO> getStocks(@RequestHeader String memberId) {
        return stockService.getStocks(Long.valueOf(memberId));
    }

    @GetMapping("/search/stock/{code}")
    public StockDetailDTO getStock(@PathVariable String code,
                                   @RequestParam(defaultValue = "1M") String period,
                                   @RequestHeader String memberId) {
        return stockService.getStockByCode(code, period, Long.valueOf(memberId));
    }

    @GetMapping("/search/stocks/keyword")
    public List<StocksDTO> searchStocks(@RequestParam String query) {
        System.out.println("query === " + query);
        return stockService.searchSimilarStocks(query);
    }
}
