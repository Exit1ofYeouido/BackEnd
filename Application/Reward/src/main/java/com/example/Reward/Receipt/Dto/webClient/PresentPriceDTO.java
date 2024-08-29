package com.example.Reward.Receipt.Dto.webClient;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresentPriceDTO {

    private Output output;

    @Getter
    @NoArgsConstructor
    public static class Output {
        private Integer stck_prpr;
    }
}
