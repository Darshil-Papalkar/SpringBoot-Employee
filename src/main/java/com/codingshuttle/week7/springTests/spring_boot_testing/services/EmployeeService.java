package com.codingshuttle.week7.springTests.spring_boot_testing.services;

import com.codingshuttle.week7.springTests.spring_boot_testing.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createNewEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long id);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    void deleteEmployee(Long id);

}
