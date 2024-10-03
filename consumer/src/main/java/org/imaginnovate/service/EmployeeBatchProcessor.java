package org.imaginnovate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.imaginnovate.sheduler.BatchScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class EmployeeBatchProcessor {
    @Value("${spring.activemq.topic}")
    String topic;

    @Value("${spring.activemq.queue}")
    String queue;

    @Autowired
    JmsTemplate jmsTemplate;

    private final BatchScheduler batchScheduler;

    public EmployeeBatchProcessor(BatchScheduler batchScheduler) {
        this.batchScheduler = batchScheduler;
    }

//    @Scheduled(cron = "0 */2 * * * ?")
    public void sendToQueue() throws JsonProcessingException {
        try {
          String jsonObj ="Creating batch request";
            jmsTemplate.send(queue, messageCreator -> {
                TextMessage message = messageCreator.createTextMessage();
                System.out.println(jsonObj);
                batchScheduler.runEmployeeBatchJob();
                message.setText(jsonObj);

                return message;
            });
        }
        catch (Exception ex) {
            System.out.println("ERROR in sending message to queue");
        }
    }

//    @Scheduled(cron = "0  */5 * * ?") // Every day at 11:30 AM
//    @Transactional
//    public void processEmployeeFile() {
//        BatchProcess batchProcess = new BatchProcess();
//        batchProcess.setProcessName("Employee Upsert batch process");
//        batchProcess.setStartTimestamp(LocalDateTime.now());
//
//        int insertedCount = 0;
//        int updatedCount = 0;
//        int errorCount = 0;
//
//        String filePath = "src/main/resources/employees.csv'"; // Adjust the path to your CSV file
//
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] data = line.split(",");
//                String employeeId = data[0]; // Assuming CSV Order: EmployeeID, FirstName, LastName, Email, PhoneNumber, DOJ, Salary
//
//                try {
//                    boolean isUpdated = processEmployeeRecord(data);
//                    if (isUpdated) {
//                        updatedCount++;
//                    } else {
//                        insertedCount++;
//                    }
//                } catch (Exception e) {
//                    logError(employeeId, e.getMessage());
//                    errorCount++;
//                }
//            }
//        } catch (IOException e) {
//            // Handle file reading exceptions
//            e.printStackTrace();
//        } finally {
//            batchProcess.setInsertedRecordCount(insertedCount);
//            batchProcess.setUpdatedRecordCount(updatedCount);
//            batchProcess.setErroredRecordCount(errorCount);
//            batchProcess.setEndTimestamp(LocalDateTime.now());
//            batchProcessRepository.save(batchProcess);
//            renameProcessedFile(filePath);
//        }
//    }
//
//    private boolean processEmployeeRecord(String[] data) {
//        String employeeId = data[0];
//        String firstName = data[1];
//        String lastName = data[2];
//        String email = data[3];
//        String phoneNumber = data[6];
//        String doj = data[4];
//        double salary = Double.parseDouble(data[5]);
//
//        Employee employee = employeeRepository.findByEmployeeId(employeeId).orElse(new Employee());
//        boolean isUpdated = employee.getId() != null; // Check if employee exists
//
//        // Set employee fields
//        employee.setEmployeeId(employeeId);
//        employee.setFirstName(firstName);
//        employee.setLastName(lastName);
//        employee.setEmail(email);
//        employee.setDoj(doj);
//        employee.setSalary(salary);
//
//        // Handle phone numbers
//        Set<PhoneNumber> phoneNumbers = new HashSet<>();
//        PhoneNumber phone = new PhoneNumber();
//        phone.setNumber(phoneNumber);
//        phone.setEmployee(employee);
//        phoneNumbers.add(phone);
//        employee.setPhoneNumbers(phoneNumbers);
//
//        // Save employee (insert or update)
//        employeeRepository.save(employee);
//
//        return isUpdated; // Return true if updated, false if inserted
//    }
//
//    private void logError(String employeeId, String actualError) {
//        ErrorLog errorLog = new ErrorLog();
//        errorLog.setTimestamp(LocalDateTime.now());
//        errorLog.setFilename("employees.csv");
//        errorLog.setMessage("Error processing Employee ID: " + employeeId);
//        errorLog.setActualError(actualError);
//        errorLogRepository.save(errorLog);
//    }
//
//    private void renameProcessedFile(String originalFilePath) {
//        try {
//            Path sourcePath = Paths.get(originalFilePath);
//            String newFileName = "processed-employees-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yy-HH-mm-ss")) + ".csv";
//            Path destinationPath = Paths.get(sourcePath.getParent().toString(), newFileName);
//            Files.move(sourcePath, destinationPath);
//            System.out.println("Processed file renamed to: " + newFileName);
//        } catch (IOException e) {
//            e.printStackTrace(); // Handle file rename exceptions
//        }
//    }
}