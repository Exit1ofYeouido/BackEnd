package com.example.Home.Member;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private Long point;

    public Long getMemberId() {
        return memberId;
    }

    public Long getPoint() {
        return point;
    }
}
