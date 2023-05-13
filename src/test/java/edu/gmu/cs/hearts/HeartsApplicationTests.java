package edu.gmu.cs.hearts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HeartsApplicationTests {

	@Test
	void contextLoads() {
			int i = 2+2;
		assertEquals(4,i);
	}

}
