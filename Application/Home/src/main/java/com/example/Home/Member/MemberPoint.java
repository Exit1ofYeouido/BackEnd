package com.example.Home.Member;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "account")
public class MemberPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer point;

    private String accountNumber;

    private LocalDateTime createdAt;

}