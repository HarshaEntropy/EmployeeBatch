package org.imaginnovate.config;

import lombok.AllArgsConstructor;
import org.imaginnovate.entity.Employee;
import org.imaginnovate.processer.EmployeeItemProcessor;
import org.imaginnovate.reader.EmployeeItemReader;
import org.imaginnovate.writer.EmployeeItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class BatchConfig {

    private final EmployeeItemReader employeeItemReader;
    private final EmployeeItemProcessor employeeItemProcessor;
    private final EmployeeItemWriter employeeItemWriter;

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-step", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(employeeItemReader.itemReader())
                .processor(employeeItemProcessor)
                .writer(employeeItemWriter.itemWriter())
                .faultTolerant()
                .retryLimit(3) // Number of retry attempts
                .retry(Exception.class) // Replace with specific exception if needed
                .skip(Exception.class) // To skip non-recoverable errors
                .skipLimit(5)
                .taskExecutor(taskExecutor())
                .build();
    }

    private TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);  // Limit concurrency to 5
        return asyncTaskExecutor;
    }

    @Bean(name = "csvJob")
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("csv-job", jobRepository)
                .flow(step(jobRepository, transactionManager)).end().build();
    }
}
