package com.codingshuttle.week7.springTests.spring_boot_testing.controllers;

import com.codingshuttle.week7.springTests.spring_boot_testing.TestContainerConfiguration;
import com.codingshuttle.week7.springTests.spring_boot_testing.dto.EmployeeDto;
import com.codingshuttle.week7.springTests.spring_boot_testing.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTestClass {

    @Autowired
    WebTestClient webTestClient;

    Employee employee = Employee.builder()
            .id(1L)
            .email("test@test.com")
            .name("Test")
            .salary(200L)
            .build();

    EmployeeDto employeeDto = EmployeeDto.builder()
            .id(1L)
            .email("test@test.com")
            .name("Test")
            .salary(200L)
            .build();

}
