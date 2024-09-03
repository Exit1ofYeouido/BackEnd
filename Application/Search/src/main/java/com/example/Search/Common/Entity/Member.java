package com.example.Search.Common.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private LocalDateTime birth;

    private Integer point;

    private String sex;

    private String email;

    private String phoneNumber;

    private String role;

    private String account;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}