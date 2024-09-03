package org.imaginnovate.writer;

import org.imaginnovate.entity.Employee;
import org.imaginnovate.repository.EmployeeRepository;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EmployeeItemWriter {

    private final EmployeeRepository employeeRepository;

    public EmployeeItemWriter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public RepositoryItemWriter<Employee> itemWriter() {
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeeRepository);
        writer.setMethodName("save");  // Method to save or update records
        return writer;
    }
}
