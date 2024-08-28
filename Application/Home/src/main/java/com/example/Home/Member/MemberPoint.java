package com.example.Home.Member;

import jakarta.persistence.*;

@Entity
@Table(name = "member_point", schema = "mo")
public class MemberPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_point_id;

    //    @Column(name = "member_id")
    private int amount;

    @Column(name = "result_point")
    private int resultPoint;

    public Long getMember_point_id() {
        return member_point_id;
    }

    public int getAmount() {
        return amount;
    }

    public int getResultPoint() {
        return resultPoint;
    }
}