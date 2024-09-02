package com.example.Home.Member;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
    private Long point;

    public Long getId() {
        return id;
    }

    public Long getPoint() {
        return point;
    }
}
