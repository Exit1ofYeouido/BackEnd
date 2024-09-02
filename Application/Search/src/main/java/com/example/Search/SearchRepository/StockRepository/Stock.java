package com.example.Search.SearchRepository.StockRepository;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stock", schema = "mo" , indexes = {
        @Index(name = "idx_stock_name",columnList = "name")
})
public class Stock {

    @Id
    @Column(name = "code", columnDefinition = "char(6)")
    private String code;

    @Column(name = "name")
    private String name;

}