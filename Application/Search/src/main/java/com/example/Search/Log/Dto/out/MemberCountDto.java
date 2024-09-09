package com.example.Search.Log.Dto.out;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCountDto {
    private String enterpriseName;
    @Setter
    private boolean isHolding;
    private Long totalCount;

    public MemberCountDto(String enterpriseName, Long totalCount) {
        this.enterpriseName = enterpriseName;
        this.totalCount = totalCount;
    }
}
