package org.imaginnovate.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String doj;
    private double salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<PhoneNumber> phoneNumbers;

    // Getters and Setters
}

