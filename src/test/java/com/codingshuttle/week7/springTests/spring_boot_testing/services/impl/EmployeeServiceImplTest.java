package com.codingshuttle.week7.springTests.spring_boot_testing.services.impl;

import com.codingshuttle.week7.springTests.spring_boot_testing.TestContainerConfiguration;
import com.codingshuttle.week7.springTests.spring_boot_testing.dto.EmployeeDto;
import com.codingshuttle.week7.springTests.spring_boot_testing.entities.Employee;
import com.codingshuttle.week7.springTests.spring_boot_testing.exceptions.ResourceNotFoundException;
import com.codingshuttle.week7.springTests.spring_boot_testing.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .email("test@test.com")
                .name("Test")
                .salary(200L)
                .build();

        employeeDto = modelMapper.map(employee, EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_whenEmployeeIdIsPresent_thenReturnEmployeeDto() {
        Long id = 1L;

        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(employee));

        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(employee.getId());
        assertThat(employeeDto.getEmail()).isEqualTo(employee.getEmail());

        verify(employeeRepository, atLeast(1)).findById(id);
        verify(employeeRepository, only()).findById(id);
        verify(employeeRepository, atMost(5)).findById(id);
    }

    @Test
    void testGetEmployeeById_whenEmployeeIsNotPresent_thenThrowException() {
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll())
                .thenReturn(List.of(employee));

        List<EmployeeDto> allEmployees = employeeService.getAllEmployees();

        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees).isNotEmpty();
        assertThat(allEmployees).hasSize(1);
    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_thenCreateNewEmployee() {
        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeDto savedEmployee = employeeService.createNewEmployee(employeeDto);

        assertThat(savedEmployee).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(employee.getEmail());

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException() {

        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(employee));

        assertThatThrownBy(() -> employeeService.createNewEmployee(employeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: " + employeeDto.getEmail());

        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithEmptyName_thenThrowException() {
        when(employeeRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        employeeDto.setName("");
        assertThatThrownBy(() -> employeeService.createNewEmployee(employeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee Name Cannot be Empty");

        verify(employeeRepository, times(1)).findByEmail(anyString());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(1L, employeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");


        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmployeeEmailIsSameToUpdate_thenThrowException() {
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(employee));

        employeeDto.setEmail("test1@test.com");
        assertThatThrownBy(() -> employeeService.updateEmployee(1L, employeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");


        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDtoToUpdate_thenUpdateExistingEmployee() {
        when(employeeRepository.findById(anyLong()))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        employeeDto.setSalary(500L);
        EmployeeDto updateEmployee = employeeService.updateEmployee(1L, employeeDto);

        assertThat(updateEmployee).isNotNull();
        assertThat(updateEmployee).isEqualTo(employeeDto);
        assertThat(updateEmployee.getId()).isEqualTo(employeeDto.getId());
        assertThat(updateEmployee.getEmail()).isEqualTo(employeeDto.getEmail());

        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeRepository).save(any());
    }

    @Test
    void testDeleteEmployee_whenEmployeeNotExists_thenThrowException() {
        when(employeeRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository, times(1)).existsById(anyLong());
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee() {
        when(employeeRepository.existsById(anyLong()))
                .thenReturn(true);

        doNothing()
                .when(employeeRepository)
                .deleteById(anyLong());

        assertThatCode(() -> employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository, times(1)).existsById(anyLong());
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

}