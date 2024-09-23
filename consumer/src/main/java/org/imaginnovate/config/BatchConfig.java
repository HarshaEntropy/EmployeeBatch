package org.imaginnovate.config;

import lombok.AllArgsConstructor;
import org.imaginnovate.entity.BatchProcess;
import org.imaginnovate.entity.Employee;
import org.imaginnovate.processer.EmployeeItemProcessor;
import org.imaginnovate.reader.EmployeeItemReader;
import org.imaginnovate.repository.BatchProcessRepository;
import org.imaginnovate.repository.EmployeeRepository;
import org.imaginnovate.writer.EmployeeItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@AllArgsConstructor
//@Configuration.enforceUniqueMethods
public class BatchConfig {

    private final EmployeeRepository employeeRepository;

    private final BatchProcessRepository batchProcessRepository;

    @Bean
    public EmployeeItemWriter employeeItemWriter(Long batchProcessId){
        return new EmployeeItemWriter(employeeRepository,batchProcessRepository,batchProcessId);
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
        BatchProcess batchProcess = new BatchProcess();
        batchProcess.setProcessName("CSV Import Process");
        batchProcess.setStartTimestamp(LocalDateTime.now());
        batchProcess.setStatus("In Progress"); // Initial status
        BatchProcess savedBatchProcess = batchProcessRepository.save(batchProcess); // Use your repository to save the status

        return new JobBuilder("csv-job", jobRepository)
                .flow(step(jobRepository, transactionManager, savedBatchProcess.getId())) // Pass ID to writer
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        // Update BatchProcess status upon completion
                        employeeItemWriter(0l).completeBatch(jobExecution.getStatus().toString());
                    }
                })
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, Long batchProcessId) {
        return new StepBuilder("csv-step", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(employeeItemReader())
                .processor(employeeItemProcessor())
                .writer(employeeItemWriter(batchProcessId)) // Inject Batch Process ID
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .skip(Exception.class)
                .skipLimit(5)
                .taskExecutor(taskExecutor())
                .build();
    }

}
