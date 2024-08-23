package com.example.Reward.Advertisement.Entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter

public class Quiz {



    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String choice1;

    private String choice2;

    private String choice3;

    private String choice4;

    private int answer;

    @ManyToOne
    @JoinColumn(name="media_link_id")
    private MediaLink mediaLink;

}
