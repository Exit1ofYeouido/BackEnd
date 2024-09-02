package com.example.Home.Member;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "account")
public class MemberPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;


    @Column(name = "point")
    private int point;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}