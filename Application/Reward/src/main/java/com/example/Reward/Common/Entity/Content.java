package com.example.Reward.Common.Entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Content {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="content_id")
    private Long id;


    private String type;
}
