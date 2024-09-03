package org.imaginnovate.reader;

import org.imaginnovate.config.EmployeeFieldSetMapper;
import org.imaginnovate.entity.Employee;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class EmployeeItemReader {

    @Bean
    public FlatFileItemReader<Employee> itemReader() {
        FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/employees.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);  // Skip header row
        itemReader.setLineMapper(lineMapper());
        return itemReader;
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
}
