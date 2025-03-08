package com.codingshuttle.week7.springTests.spring_boot_testing.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dptTitle;

    public Department() {}

    public Department(String dptTitle) {
        this.dptTitle = dptTitle;
    }

}
