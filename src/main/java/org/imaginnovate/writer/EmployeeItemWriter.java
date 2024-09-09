package org.imaginnovate.writer;

import org.imaginnovate.entity.Employee;
import org.imaginnovate.repository.EmployeeRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeItemWriter implements ItemWriter<Employee> {

    private final EmployeeRepository employeeRepository;
    private final RepositoryItemWriter<Employee> repositoryItemWriter;

    @Autowired
    public EmployeeItemWriter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.repositoryItemWriter = createRepositoryItemWriter();
    }

    private RepositoryItemWriter<Employee> createRepositoryItemWriter() {
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeeRepository);
        writer.setMethodName("save"); // Method to save or update records
        return writer;
    }

//    @Override
//    public void write(List<? extends Employee> items) throws Exception {
//        repositoryItemWriter.write(items);
//    }

    @Override
    public void write(Chunk<? extends Employee> chunk) throws Exception {
        repositoryItemWriter.write(chunk);
    }
}
