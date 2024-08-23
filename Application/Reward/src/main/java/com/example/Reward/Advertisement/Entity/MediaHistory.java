package com.example.Reward.Advertisement.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaHistory{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name="media_link_id")
    private MediaLink mediaLink;

}
