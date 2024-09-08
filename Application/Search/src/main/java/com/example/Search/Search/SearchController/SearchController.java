package com.example.Search.Search.SearchController;


import com.example.Search.Log.Service.LogService;
import com.example.Search.Search.Api.KisService;
import com.example.Search.Search.SearchDTO.StockDetailDTO;
import com.example.Search.Search.SearchDTO.StockPriceListDTO;
import com.example.Search.Search.SearchDTO.StocksDTO;
import com.example.Search.Search.SearchService.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "서치 API")
public class SearchController {

    private final SearchService searchService;
    private final KisService kisService;
    private final LogService logService;

    public SearchController(SearchService searchService, KisService kisService, LogService logService) {
        this.searchService = searchService;
        this.kisService = kisService;
        this.logService = logService;
    }

    @GetMapping("/stocks")
    public List<StocksDTO> getStocks(@RequestHeader String memberId) {
        return searchService.getStocks(Long.valueOf(memberId));
    }

    @GetMapping("/stock/{code}")
    public StockDetailDTO getStock(@PathVariable String code,
                                   @RequestHeader String memberId) {
        logService.recordSearchLog(Long.valueOf(memberId), code);
        return searchService.getStockByCode(code, Long.valueOf(memberId));
    }

    @GetMapping("/stockPriceList/{code}")
    public StockPriceListDTO getStockPriceList(@PathVariable String code,
                                               @RequestParam(defaultValue = "1M") String period) {

        return searchService.getStockPriceList(code, period);
    }

    @GetMapping("/stocks/keyword")
    public List<StocksDTO> searchStocks(@RequestParam String query) {
        return searchService.searchSimilarStocks(query);
    }
}
