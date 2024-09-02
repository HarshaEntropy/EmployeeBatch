package org.imaginnovate.service;

import org.imaginnovate.entity.Employee;
import org.imaginnovate.entity.PhoneNumber;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        // Ensure DOJ is properly set and formatted
        if (employee.getDoj() == null) {
            throw new IllegalArgumentException("Date of Joining (DOJ) is required for Employee ID: " + employee.getEmployeeId());
        }

        // Validate the salary
        if (employee.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salary must be greater than zero for Employee ID: " + employee.getEmployeeId());
        }

        // Process phone numbers (split by delimiter and validate format)
        String phoneNumbers = employee.getPhoneNumbersAsString();
        if (phoneNumbers != null && !phoneNumbers.trim().isEmpty()) {
            List<PhoneNumber> validPhoneNumbers = new ArrayList<>();
            String[] phoneNumberArray = phoneNumbers.split(";"); // Adjust delimiter if necessary
            for (String phoneNumber : phoneNumberArray) {
                phoneNumber = phoneNumber.trim();
                if (isValidPhoneNumber(phoneNumber)) {
                    validPhoneNumbers.add(new PhoneNumber(phoneNumber, employee));
                } else {
                    throw new IllegalArgumentException("Invalid phone number for Employee ID: " + employee.getEmployeeId());
                }
            }
            employee.setPhoneNumbers(validPhoneNumbers);
        } else {
            employee.setPhoneNumbers(new ArrayList<>()); // Ensure phoneNumbers is initialized if not provided
        }

        return employee; // Return processed employee data
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Basic validation for phone number format, adjust as necessary for your use case
        return phoneNumber.matches("\\+1-\\d{3}-\\d{3}-\\d{4}");
    }
}
