package com.codingshuttle.week7.springTests.spring_boot_testing;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class SpringBootTestingApplicationTests {

	@BeforeEach
	void setUp() {
		log.info("Starting the method, setting up config");
	}

	@AfterEach
	void tearDown() {
		log.info("Tearing down the method");
	}

	@BeforeAll
	static void setUpOnce() {
		log.info("Starting the setup once...");
	}

	@AfterAll
	static void tearDownOnce() {
		log.info("Tearing down the method once....");
	}

	@Test
//	@Disabled
	void testNumberOne() {
		int a = 5;
		int b = 3;
		int result = addTwoNumber(a, b);

//		Assertions.assertEquals(8, result);

		assertThat(result)
				.isEqualTo(8)
				.isCloseTo(9, Offset.offset(1));

		assertThat("Apple")
				.startsWith("App")
				.endsWith("le")
				.hasSize(5);
	}

	@Test
//	@DisplayName("displayTestNameTwo")
	void testNumberTwo() {
		int a = 5;
		int b = 3;

		double result = divideTwoNumbers(a, b);

		log.info("Result: {}", result);
	}

	@Test
//	@DisplayName("displayTestNameTwo")
	void testDivideTwoNumbers_whenDenominatorIsZero_ThenArithmeticException() {
		int a = 5;
		int b = 0;

		assertThatThrownBy(() -> divideTwoNumbers(a, b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("Tried to divide by zero");
	}

	int addTwoNumber(int a, int b) {
		return a + b;
	}

	double divideTwoNumbers(int a, int b) {
		try {
			return a/b;
		} catch (ArithmeticException exception) {
			log.error("Arithmetic exception occurred: {}", exception.getLocalizedMessage());
			throw new ArithmeticException("Tried to divide by zero");
		}
	}

}
