package com.example.Enterprise.Entity;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue
    private String code;

    private String name;



}
