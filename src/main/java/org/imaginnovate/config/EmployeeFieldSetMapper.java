package org.imaginnovate.config;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.imaginnovate.entity.Employee;
import org.imaginnovate.entity.PhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class EmployeeFieldSetMapper implements FieldSetMapper<Employee> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Employee mapFieldSet(FieldSet fieldSet) {
        Employee employee = new Employee();
        employee.setEmployeeId(fieldSet.readLong("employeeId"));
        employee.setFirstName(fieldSet.readString("firstName").trim());
        employee.setLastName(fieldSet.readString("lastName").trim());
        employee.setEmail(fieldSet.readString("email").trim());
        employee.setDoj(LocalDate.parse(fieldSet.readString("doj"), DATE_FORMATTER));
        employee.setSalary(new BigDecimal(fieldSet.readString("salary")));

        // Process phone numbers
        String phoneNumbers = fieldSet.readString("phoneNumbers").trim();
        employee.setPhoneNumbersAsString(phoneNumbers);

        return employee;
    }
}
