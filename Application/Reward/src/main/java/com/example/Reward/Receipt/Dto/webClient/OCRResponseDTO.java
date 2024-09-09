package com.example.Reward.Receipt.Dto.webClient;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class OCRResponseDTO {

    private Images[] images;

    public OCRResponseDTO(Images[] images) {
        this.images = images;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    public static class Images {
        private Receipt receipt;

        public Images(Receipt receipt) {
            this.receipt = receipt;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        public static class Receipt {
            private Result result;

            public  Receipt(Result result) {
                this.result = result;
            }

            @Getter
            @Builder
            @NoArgsConstructor
            public static class Result {
                private StoreInfo storeInfo;
                private TotalPrice totalPrice;
                private PaymentInfo paymentInfo;

                public Result(StoreInfo storeInfo, TotalPrice totalPrice, PaymentInfo paymentInfo) {
                    this.paymentInfo = paymentInfo;
                    this.storeInfo = storeInfo;
                    this.totalPrice = totalPrice;
                }

                @Getter
                @Builder
                @NoArgsConstructor
                public static class StoreInfo {
                    private Name name;

                    public StoreInfo(Name name) {
                        this.name = name;
                    }

                    @Getter
                    @Builder
                    @NoArgsConstructor
                    public static class Name {
                        private String text;

                        public Name(String text) {
                            this.text = text;
                        }
                    }
                }

                @Getter
                @Builder
                @NoArgsConstructor
                public static class TotalPrice {
                    private Price price;

                    public TotalPrice(Price price) {
                        this.price = price;
                    }

                    @Getter
                    @Builder
                    @NoArgsConstructor
                    public static class Price {
                        private String text;

                        public Price(String text) {
                            this.text = text;
                        }
                    }
                }

                @Getter
                @Builder
                @NoArgsConstructor
                public static class PaymentInfo {
                    private Date date;
                    private Time time;
                    private ConfirmNum confirmNum;

                    public PaymentInfo(Date date, Time time, ConfirmNum confirmNum) {
                        this.confirmNum = confirmNum;
                        this.date = date;
                        this.time = time;
                    }

                    @Getter
                    @Builder
                    @NoArgsConstructor
                    public static class Date {
                        private String text;
                        public Date(String text) {
                            this.text = text;
                        }
                    }

                    @Getter
                    @Builder
                    @NoArgsConstructor
                    public static class Time {
                        private String text;

                        public Time(String text) {
                            this.text = text;
                        }
                    }

                    @Getter
                    @Builder
                    @NoArgsConstructor
                    public static class ConfirmNum {
                        private String text;

                        public ConfirmNum(String text) {
                            this.text = text;
                        }
                    }
                }
            }
        }
    }
}
