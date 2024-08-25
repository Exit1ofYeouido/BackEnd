package com.example.Reward;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RewardApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(Math.round((150.0 / 75000) * 1000000) / 1000000.0);
	}

}
