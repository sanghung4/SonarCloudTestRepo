package com.reece.punchoutcustomerbff;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.boot.SpringApplication;

@PrepareForTest(PunchoutCustomerBffApplication.class)
class PunchoutCustomerBffApplicationTests {

	private MockedStatic<SpringApplication> mockSpring;

	@BeforeEach
	public void before() {
		new PunchoutCustomerBffApplication();
		mockSpring = mockStatic(SpringApplication.class);
	}

	@AfterEach
	public void after() {
		mockSpring.close();
	}

	@Test
	public void testMain() {
		// given
		String[] args = new String[] {};

		// when
		PunchoutCustomerBffApplication.main(args);

		// then
		SpringApplication.run(PunchoutCustomerBffApplication.class, args);
	}

}
