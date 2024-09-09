package org.imaginnovate.writer;

import org.imaginnovate.entity.BatchProcess; // Import the BatchProcess
import org.imaginnovate.entity.Employee;
import org.imaginnovate.repository.BatchProcessRepository; // Repository for BatchProcess
import org.imaginnovate.repository.EmployeeRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EmployeeItemWriter implements ItemWriter<Employee> {

    private final EmployeeRepository employeeRepository;
    private final RepositoryItemWriter<Employee> repositoryItemWriter;
    private final BatchProcessRepository batchProcessRepository; // Add this line
    private final Long batchProcessId; // Field to track the current batch process

    @Autowired
    public EmployeeItemWriter(EmployeeRepository employeeRepository, BatchProcessRepository batchProcessRepository, Long batchProcessId) {
        this.employeeRepository = employeeRepository;
        this.batchProcessRepository = batchProcessRepository; // Initialize the BatchProcessRepository
        this.batchProcessId = batchProcessId; // Initialize the batchProcessId
        this.repositoryItemWriter = createRepositoryItemWriter();
    }

    private RepositoryItemWriter<Employee> createRepositoryItemWriter() {
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeeRepository);
        writer.setMethodName("save"); // Method to save or update records
        return writer;
    }

    @Override
    public void write(Chunk<? extends Employee> chunk) throws Exception {
        repositoryItemWriter.write(chunk);
    }

    // New method to update BatchProcess status
    public void completeBatch(String status) {
        BatchProcess batchProcess = batchProcessRepository.findById(batchProcessId)
                .orElseThrow(() -> new RuntimeException("BatchProcess not found"));
        batchProcess.setEndTimestamp(LocalDateTime.now());
        batchProcess.setStatus(status);
        batchProcessRepository.save(batchProcess);
    }
}
