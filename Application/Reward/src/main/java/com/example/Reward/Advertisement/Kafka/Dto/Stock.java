package com.example.Reward.Advertisement.Kafka.Dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;


@Getter
public class Stock {


    private String code;

    private String name;
}
