package com.codingshuttle.week7.springTests.spring_boot_testing;

import com.codingshuttle.week7.springTests.spring_boot_testing.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringBootTestingApplication implements CommandLineRunner {

//	private final DataService dataService;

	@Value("${my.variable}")
	private String myVariable;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTestingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("My Variable: " + myVariable);

//		System.out.println("The Data is: " + dataService.getData());
	}
}
