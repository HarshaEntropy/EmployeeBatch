package org.imaginnovate.config;

import lombok.AllArgsConstructor;
import org.imaginnovate.entity.Employee;
import org.imaginnovate.processer.EmployeeItemProcessor;
import org.imaginnovate.reader.EmployeeItemReader;
import org.imaginnovate.repository.EmployeeRepository;
import org.imaginnovate.writer.EmployeeItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@AllArgsConstructor
public class BatchConfig {

    private final EmployeeRepository employeeRepository;

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-step", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(employeeItemReader())
                .processor(employeeItemProcessor())
                .writer(employeeItemWriter())
                .faultTolerant()
                .retry(Exception.class) // Consider specific exceptions for retry
                .retryLimit(3)
                .skip(Exception.class) // Consider specific exceptions for skip
                .skipLimit(5)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public ItemWriter<Employee> employeeItemWriter(){
        return new EmployeeItemWriter(employeeRepository);
    }

    @Bean
    public ItemReader<Employee> employeeItemReader(){
        return new EmployeeItemReader();
    }

    @Bean
    public ItemProcessor<Employee, Employee> employeeItemProcessor(){
        return new EmployeeItemProcessor(employeeRepository);
    }

    private TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set accordingly
        executor.setMaxPoolSize(10); // Adjust based on requirements
        executor.setQueueCapacity(25); // Adjust based on requirements
        executor.initialize();
        return executor;
    }

    @Bean(name = "csvJob")
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("csv-job", jobRepository)
                .flow(step(jobRepository, transactionManager))
                .end()
                .build();
    }
}
