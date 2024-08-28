package com.example.Reward.Receipt.Dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OCRResponseDTO {

    private StoreInfo storeInfo;
    private TotalPrice totalPrice;
    private PaymentInfo paymentInfo;

    @Getter
    public static class StoreInfo {
        private Name name;

        @Getter
        public static class Name {
            private String text;
        }
    }

    @Getter
    public static class TotalPrice {
        private Price price;

        @Getter
        public static class Price {
            private String text;
        }
    }

    @Getter
    public static class PaymentInfo {
        private Date date;
        private Time time;
        private ConfirmNum confirmNum;

        @Getter
        public static class Date {
            public String text;
        }

        @Getter
        public static class Time {
            private String text;
        }

        @Getter
        public static class ConfirmNum {
            private String text;
        }
    }

}
