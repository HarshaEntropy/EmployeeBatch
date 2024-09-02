package org.imaginnovate.contoller;

import org.imaginnovate.entity.Employee;
import org.imaginnovate.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/api/employees")
    public ResponseEntity<Employee> getEmployee(@RequestParam(required = false) Long employeeId,
                                                @RequestParam(required = false) String email) {
        Employee employee = null;

        if (employeeId != null) {
            employee = employeeRepository.findByEmployeeId(employeeId).orElse(null);
        } else if (email != null) {
            employee = employeeRepository.findByEmail(email).orElse(null);
        }

        return ResponseEntity.ok(employee);
    }
}