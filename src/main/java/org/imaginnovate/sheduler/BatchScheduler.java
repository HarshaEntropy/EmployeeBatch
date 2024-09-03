package org.imaginnovate.sheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher; // JobLauncher to run the batch job
    private final Job employeeJob; // Inject the job configured in BatchConfig

    @Scheduled(cron = "0 */2 * * * ?") // Every day at 11:30 AM     //TODO change to 11:30 @Scheduled(cron = "0 30 11 * * ?")
    public void runEmployeeBatchJob() {
        try {
            // Create job parameters if needed, you can pass additional params here
            JobParametersBuilder params = new JobParametersBuilder();
            params.addLong("startAt", System.currentTimeMillis());

            // Launch the job
            jobLauncher.run(employeeJob, params.toJobParameters());

            System.out.println("Employee batch job executed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while running employee batch job: " + e.getMessage());
        }
    }
}