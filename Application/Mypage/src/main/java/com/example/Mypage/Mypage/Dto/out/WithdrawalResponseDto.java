package com.example.Mypage.Mypage.Dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WithdrawalResponseDto {

    private int remainPoint;
}
