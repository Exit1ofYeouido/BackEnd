package com.example.Mypage.Common.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberStock {


    @Id
    @GeneratedValue
    private Long id;

    private double count;


    private int averagePrice;

    @Column(unique = true)
    private String stockCode;

    @Column(unique = true)
    private String stockName;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;




}
