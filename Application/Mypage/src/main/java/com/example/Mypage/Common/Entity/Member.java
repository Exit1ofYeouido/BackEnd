package com.example.Mypage.Common.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private LocalDateTime birth;

    private Long point;

    private String sex;

    private String email;

    private String phoneNumber;

    private String role;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
