package com.example.Reward.Receipt.Dto.webClient;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class StarbucksResponseDTO {
    private Images[] images;

    public StarbucksResponseDTO(Images[] images) {
        this.images = images;
    }

    @Getter
    @NoArgsConstructor
    public static class Images {
        private Fields[] fields;

        public Images(Fields[] fields) {
            this.fields = fields;
        }

        @Getter
        @NoArgsConstructor
        public static class Fields {
            private String inferText;
        }
    }
}
