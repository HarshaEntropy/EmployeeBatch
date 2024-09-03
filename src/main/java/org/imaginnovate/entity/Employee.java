package org.imaginnovate.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude = "phoneNumbers") // Exclude phoneNumbers to avoid circular reference
@ToString(exclude = "phoneNumbers") // Exclude phoneNumbers from toString() to prevent infinite recursion
public class Employee {
    @Id
    private Long employeeId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate doj;

    @Column(nullable = false)
    private BigDecimal salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Prevents infinite recursion in JSON serialization
    private List<PhoneNumber> phoneNumbers;

    @Transient
    private String phoneNumbersAsString;
}
