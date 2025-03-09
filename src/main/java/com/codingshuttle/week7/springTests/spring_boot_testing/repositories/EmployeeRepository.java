package com.codingshuttle.week7.springTests.spring_boot_testing.repositories;

import com.codingshuttle.week7.springTests.spring_boot_testing.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

}
