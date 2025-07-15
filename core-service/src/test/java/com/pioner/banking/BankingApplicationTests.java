package com.pioner.banking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerConfig.Initializer.class)
class BankingApplicationTests {

	@Test
	void contextLoads() {
	}

}
