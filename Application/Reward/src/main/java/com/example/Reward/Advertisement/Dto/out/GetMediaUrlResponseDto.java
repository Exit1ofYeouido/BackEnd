package com.example.Reward.Advertisement.Dto.out;


import com.example.Reward.Advertisement.Entity.MediaLink;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class GetMediaUrlResponseDto {

    private Long mediaId;

    private String uri;

    public static GetMediaUrlResponseDto of(Optional<MediaLink> mediaLink) {

        mediaLink.orElseThrow(()-> new RuntimeException());

        return GetMediaUrlResponseDto.builder()
                .mediaId(mediaLink.get().getId())
                .uri(mediaLink.get().getUrl()).build();


    }
}
