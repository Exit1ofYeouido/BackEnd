package com.example.Search.SearchDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrentPriceDTO {
    private Output output;

    @Getter
    @Setter
    public static class Output {

        @Getter
        private String stck_prpr;

        @Getter
        private String prdy_vrss;

        @Getter
        private String prdy_ctrt;

        public List<DailyPrice> getDailyPrices() {
            return dailyPrices;
        }

        private List<DailyPrice> dailyPrices;

    }
    @Getter
    @Setter
    public static class DailyPrice {
        private String stck_bsop_date; // 거래일자
        private String stck_clpr;      // 종가
    }
}