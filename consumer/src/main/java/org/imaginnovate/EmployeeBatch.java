package org.imaginnovate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeBatch {
    public static void main(String [] args) {
        SpringApplication.run(EmployeeBatch.class, args);
    }

}