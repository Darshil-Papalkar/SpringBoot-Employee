package com.codingshuttle.week7.springTests.spring_boot_testing.repositories;

import com.codingshuttle.week7.springTests.spring_boot_testing.TestContainerConfiguration;
import com.codingshuttle.week7.springTests.spring_boot_testing.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .name("Darshil")
                .email("darshil@test.com")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnEmployee() {

//        Arrange, Given
        employeeRepository.save(employee);

//        Act, When
        Optional<Employee> byEmail = employeeRepository.findByEmail(employee.getEmail());

//        Assert, Then
        assertThat(byEmail)
                .isNotNull()
                .isNotEmpty();
        assertThat(byEmail.isPresent()).isTrue();
        assertThat(byEmail.get().getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnOptionalEmpty() {
//        Given
        String email = "notPresent@test.com";

//        When
        Optional<Employee> byEmail = employeeRepository.findByEmail(email);

//        Then
        assertThat(byEmail)
                .isNotNull()
                .isEmpty();
        assertThat(byEmail.isPresent()).isFalse();
    }
}