package com.example.Mypage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MypageApplication {

	public static void main(String[] args) {
		SpringApplication.run(MypageApplication.class, args);
	}

}
