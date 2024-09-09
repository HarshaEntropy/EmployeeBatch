package org.imaginnovate.reader;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.imaginnovate.config.EmployeeFieldSetMapper;
import org.imaginnovate.entity.Employee;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

//import j.annotation.PostConstruct;
//import javax.annotation.PreDestroy;

@Component
public class EmployeeItemReader implements ItemReader<Employee> {

    private final FlatFileItemReader<Employee> itemReader;

    public EmployeeItemReader() {
        this.itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/employees.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1); // Skip header row
        itemReader.setLineMapper(lineMapper());
    }

    @PostConstruct
    public void initialize() {
        itemReader.open(new ExecutionContext()); // Initialize context if needed
    }

    @Override
    public Employee read() throws Exception {
        return itemReader.read(); // Read one employee at a time
    }

    private LineMapper<Employee> lineMapper() {
        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("employeeId", "firstName", "lastName", "email", "doj", "salary", "phoneNumbers");
        tokenizer.setStrict(false);

        lineMapper.setFieldSetMapper(new EmployeeFieldSetMapper());
        lineMapper.setLineTokenizer(tokenizer);
        return lineMapper;
    }

    @PreDestroy
    public void close() {
        itemReader.close(); // Close the reader when the application context is destroyed
    }
}
