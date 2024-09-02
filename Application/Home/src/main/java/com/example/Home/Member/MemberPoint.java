package com.example.Home.Member;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class MemberPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;


    @Column(name = "point")
    private int resultPoint;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Long getId() {
        return id;
    }


    public int getResultPoint() {
        return resultPoint;
    }
}