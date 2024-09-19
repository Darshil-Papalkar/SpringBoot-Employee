package com.codingshuttle.week7.springTests.spring_boot_testing.controllers;

import com.codingshuttle.week7.springTests.spring_boot_testing.dto.EmployeeDto;
import com.codingshuttle.week7.springTests.spring_boot_testing.entities.Employee;
import com.codingshuttle.week7.springTests.spring_boot_testing.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmployeeControllerTestIT extends AbstractIntegrationTestClass {


    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_success() {
        Employee savedEmployee = employeeRepository.save(employee);


        webTestClient.get()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
//                .value(employeeDto1 -> {
//                    assertThat(employeeDto1.getEmail()).isEqualTo(employee.getEmail());
//                    assertThat(employeeDto1.getId()).isEqualTo(employeeDto.getId());
//                });
    }

    @Test
    void testGetEmployeeById_Failure() {

        webTestClient.get()
                .uri("/employees/{id}", employee.getId())
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException() {
        Employee employee1 = employeeRepository.save(employee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee() {

        webTestClient.post()
                .uri("/employees")
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(employeeDto.getName());

    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {

        webTestClient.put()
                .uri("/employees/{id}", employee.getId())
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().isNotFound();

    }


    @Test
    void testUpdateEmployee_whenEmployeeEmailDoesNotMatchWithUpdateEmail_thenThrowException() {
        Employee savedEmployee = employeeRepository.save(employee);
        employeeDto.setEmail("test1@test.com");

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesExists_thenUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);

        employeeDto.setId(savedEmployee.getId());
        employeeDto.setName("Test2");
        employeeDto.setSalary(500L);

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(employeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(employeeDto);
//                .jsonPath("$.id").isEqualTo(employeeDto.getId())
//                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());

    }

    @Test
    void testDeleteEmployee_whenEmployeeIdDoesNotExists_thenThrowException() {

        webTestClient.delete()
                .uri("/employees/{id}", employee.getId())
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testDeleteEmployee_whenEmployeeIdDoesExists_thenDeleteEmployee() {
        employeeRepository.save(employee);

        webTestClient.delete()
                .uri("/employees/{id}", employee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        webTestClient.delete()
                .uri("/employees/{id}", employee.getId())
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testGetAllEmployees() {

        webTestClient.get()
                .uri("/employees")
                .exchange()
                .expectStatus().isOk();

    }

}