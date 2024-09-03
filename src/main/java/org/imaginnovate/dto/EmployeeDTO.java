package org.imaginnovate.dto;

import org.imaginnovate.entity.Employee;
import org.imaginnovate.entity.PhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDTO {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate doj;
    private BigDecimal salary;
    private List<String> phoneNumbers;

    // Constructor that initializes fields from the Employee entity
    public EmployeeDTO(Employee employee) {
        this.employeeId = employee.getEmployeeId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.doj = employee.getDoj();
        this.salary = employee.getSalary();
        this.phoneNumbers = employee.getPhoneNumbers().stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.toList());
    }

    // Getters and setters
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
