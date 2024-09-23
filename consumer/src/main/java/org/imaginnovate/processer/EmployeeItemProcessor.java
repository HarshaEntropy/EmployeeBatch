package org.imaginnovate.processer;

import lombok.RequiredArgsConstructor;
import org.imaginnovate.entity.Employee;
import org.imaginnovate.entity.PhoneNumber;
import org.imaginnovate.repository.EmployeeRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {
    private final EmployeeRepository employeeRepository;

    @Override
    public Employee process(Employee employee) throws Exception {
        // Validate and format the employee's first name
        String firstName = employee.getFirstName().trim();
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty for Employee ID: " + employee.getEmployeeId());
        }
        employee.setFirstName(firstName);

        // Validate and format the employee's last name
        String lastName = employee.getLastName().trim();
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty for Employee ID: " + employee.getEmployeeId());
        }
        employee.setLastName(lastName);

        // Validate and format the employee's email
        String email = employee.getEmail().trim();
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email for Employee ID: " + employee.getEmployeeId());
        }
        employee.setEmail(email);

        // Check for null DOJ
        if (employee.getDoj() == null) {
            throw new IllegalArgumentException("Date of Joining (DOJ) is required for Employee ID: " + employee.getEmployeeId());
        }

        // Validate the salary
        if (employee.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salary must be greater than zero for Employee ID: " + employee.getEmployeeId());
        }

        // Process phone numbers
        List<PhoneNumber> validPhoneNumbers = new ArrayList<>();
        String phoneNumbersAsString = employee.getPhoneNumbersAsString();

        if (phoneNumbersAsString != null && !phoneNumbersAsString.trim().isEmpty()) {
            String[] phoneNumberArray = phoneNumbersAsString.split(";");
            for (String phoneNumberStr : phoneNumberArray) {
                String phoneNumber = phoneNumberStr.trim();
                if (isValidPhoneNumber(phoneNumber)) {
                    PhoneNumber phoneNumberEntity = new PhoneNumber(phoneNumber, employee);
                    validPhoneNumbers.add(phoneNumberEntity);
                } else {
                    throw new IllegalArgumentException("Invalid phone number format for Employee ID: " + employee.getEmployeeId());
                }
            }
        }

        employee.setPhoneNumbers(validPhoneNumbers);

        // Check if employee already exists in the database
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeId(employee.getEmployeeId());

        if (existingEmployee.isPresent()) {
            // Update existing employee
            Employee existing = existingEmployee.get();
            existing.setFirstName(employee.getFirstName());
            existing.setLastName(employee.getLastName());
            existing.setEmail(employee.getEmail());
            existing.setDoj(employee.getDoj());
            existing.setSalary(employee.getSalary());
            existing.setPhoneNumbers(validPhoneNumbers);
            return existing;
        } else {
            // New employee, create and save
            return employee;
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Basic validation for phone number format, adjust as necessary for your use case
        return phoneNumber.matches("\\+1-\\d{3}-\\d{3}-\\d{4}");
    }
}
