package com.example.Reward.Advertisement.Dto.out;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInfoResponseDto {

    private Long mediaId;

    private String name;

    private String thumbnail;

    private String thumbnailName;



}
