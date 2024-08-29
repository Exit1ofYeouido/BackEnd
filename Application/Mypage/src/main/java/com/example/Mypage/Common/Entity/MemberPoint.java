package com.example.Mypage.Common.Entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MemberPoint {

    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private int amount;

    private int resultPoint;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
}
