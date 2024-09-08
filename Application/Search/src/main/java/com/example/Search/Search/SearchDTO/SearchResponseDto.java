package com.example.Search.Search.SearchDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SearchResponseDto {

    private Long currentPrice;

    private String previousPrice;
    
    private String previousRate;
}
