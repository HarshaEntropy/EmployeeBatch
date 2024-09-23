package org.imaginnovate.contoller;

import lombok.RequiredArgsConstructor;
import org.imaginnovate.dto.EmployeeDTO;
import org.imaginnovate.entity.Employee;
import org.imaginnovate.repository.EmployeeRepository;
import org.imaginnovate.sheduler.BatchScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final BatchScheduler batchScheduler;

    public EmployeeController(EmployeeRepository employeeRepository, BatchScheduler batchScheduler) {
        this.employeeRepository = employeeRepository;
        this.batchScheduler = batchScheduler;
    }

    @GetMapping("/api/employees")
    public ResponseEntity<EmployeeDTO> getEmployee(@RequestParam(required = false) Long employeeId,
                                                   @RequestParam(required = false) String email) {

        if (employeeId == null && email == null) {
            return ResponseEntity.badRequest().build();  // Return 400 Bad Request if both parameters are missing
        }

        if (employeeId != null && email != null) {
            return ResponseEntity.badRequest().build();  // Return 400 Bad Request if both parameters are provided
        }

        Employee employee = null;

        if (employeeId != null) {
            employee = employeeRepository.findByEmployeeId(employeeId).orElse(null);
        } else if (email != null) {
            employee = employeeRepository.findByEmail(email).orElse(null);
        }

        if (employee == null) {
            return ResponseEntity.notFound().build();  // Return 404 Not Found if the employee does not exist
        }

        // Convert to DTO before returning
        EmployeeDTO employeeDTO = new EmployeeDTO(employee);

        return ResponseEntity.ok(employeeDTO);
    }

    @GetMapping("/api/start-batch")
    public ResponseEntity<?> startBatch() {
        batchScheduler.runEmployeeBatchJob();
        return ResponseEntity.ok("Successful batch ");
    }

}
