package com.example.Mypage.Common.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberStock {


    @Id
    @GeneratedValue
    @Column(name = "member_stock_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private double count;

    private int averagePrice;

    @Column(unique = true, name = "code")
    private String stockCode;

    @Column(unique = true)
    private String stockName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
