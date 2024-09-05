package com.example.Reward.Advertisement.Entity;

import com.example.Reward.Common.Entity.Content;
import com.example.Reward.Common.Entity.Event;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
@Table(name="media_link")
public class MediaLink {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="media_link_id")
    private Long id;

    private String uri;

    private String thumbnail;

    private String thumbnailName;

    private String enterpriseName;


    @ManyToOne
    @JoinColumn(name="content_id")
    private Content content;


    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;



}
