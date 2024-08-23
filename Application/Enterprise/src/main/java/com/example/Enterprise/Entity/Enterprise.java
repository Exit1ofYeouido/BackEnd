package com.example.Enterprise.Entity;

import jakarta.persistence.*;

@Entity
public class Enterprise {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String intro;

    private String logo;

    @ManyToOne
    @JoinColumn(name="code")
    private Stock stock;
}
