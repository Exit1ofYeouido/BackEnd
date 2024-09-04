package com.example.Search.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_log_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "code", referencedColumnName = "code")
    private Stock stock;

    private String searchTime;
    private Boolean holding;
}
