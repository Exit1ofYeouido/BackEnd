package com.example.Reward.Common.Dto;

import com.example.Reward.Common.Entity.Event;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailEnterPriseResponseDto {

    private String name;
    private String comment;

    public static DetailEnterPriseResponseDto of(Event event) {
        return DetailEnterPriseResponseDto.builder().name(event.getEnterpriseName()).comment(event.getComment()).build();
    }
}
