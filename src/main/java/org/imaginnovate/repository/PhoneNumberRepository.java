package org.imaginnovate.repository;

import org.imaginnovate.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

    // Update the method to search by employee.employeeId
    List<PhoneNumber> findByEmployee_EmployeeId(Long employeeId);
}