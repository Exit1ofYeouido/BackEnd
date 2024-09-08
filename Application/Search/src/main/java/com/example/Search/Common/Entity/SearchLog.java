package com.example.Search.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_log_id")
    private Long id;

    private Long memberId;

    private String enterpriseName;

    private String searchTime;

    private Boolean holding;

    public SearchLog(Long memberId, String enterpriseName, String searchTime, Boolean holding) {
        this.memberId = memberId;
        this.enterpriseName = enterpriseName;
        this.searchTime = searchTime;
        this.holding = holding;
    }
}
