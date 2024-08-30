package com.example.Reward.Common.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Content {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long contentId;


    private String type;
}
