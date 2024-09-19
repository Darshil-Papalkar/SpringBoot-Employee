package com.codingshuttle.week7.springTests.spring_boot_testing.services.impl;

import com.codingshuttle.week7.springTests.spring_boot_testing.dto.EmployeeDto;
import com.codingshuttle.week7.springTests.spring_boot_testing.entities.Employee;
import com.codingshuttle.week7.springTests.spring_boot_testing.exceptions.ResourceNotFoundException;
import com.codingshuttle.week7.springTests.spring_boot_testing.repositories.EmployeeRepository;
import com.codingshuttle.week7.springTests.spring_boot_testing.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee with email: {}", employeeDto.getEmail());
        Employee existingEmployee = employeeRepository.findByEmail(employeeDto.getEmail())
                .orElse(null);

        if(existingEmployee != null) {
            log.error("Employee already exists with email: {}", employeeDto.getEmail());
            throw new RuntimeException("Employee already exists with email: " + employeeDto.getEmail());
        }

        if (employeeDto.getName().isEmpty()) {
            throw new RuntimeException("Employee Name Cannot be Empty");
        }

        Employee employee = modelMapper.map(employeeDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully created new employee with id: {}", savedEmployee.getId());
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = getEmployee(id);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        log.info("Fetching all employees");
        List<EmployeeDto> employeeDtos = employeeRepository
                .findAll()
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDto.class))
                .collect(Collectors.toList());
        log.info("Successfully fetched all employees with size of: {}", employeeDtos.size());
        return employeeDtos;
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = getEmployee(id);

        if(!employee.getEmail().equals(employeeDto.getEmail())) {
            log.error("Attempted to update email for employee with id: {}", id);
            throw new RuntimeException("The email of the employee cannot be updated");
        }

        log.info("Updating Employee Details");
        modelMapper.map(employeeDto, employee);
        employee.setId(id);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully updated employee with id: {}", id);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        boolean exists = employeeRepository.existsById(id);
        if(!exists) {
            log.error("Employee not found with id: {}", id);
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Successfully deleted employee with id: {}", id);
    }

    private Employee getEmployee(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        log.info("Successfully fetched employee with id: {}", id);
        return employee;
    }
}
