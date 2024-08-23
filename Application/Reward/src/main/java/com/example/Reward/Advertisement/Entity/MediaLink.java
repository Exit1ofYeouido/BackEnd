package com.example.Reward.Advertisement.Entity;

import com.example.Reward.Common.Entity.Content;
import com.example.Reward.Common.Entity.Event;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class MediaLink {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String thumnail;

    private String thumnailName;

    private String enterpriseName;


    @ManyToOne
    @JoinColumn(name="content_id")
    private Content content;


    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;



}
