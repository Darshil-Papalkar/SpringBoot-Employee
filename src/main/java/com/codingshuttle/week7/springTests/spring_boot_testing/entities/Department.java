package com.codingshuttle.week7.springTests.spring_boot_testing.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dptTitle;

    @OneToMany(mappedBy = "department")
    private List<Employee> employee;
}
