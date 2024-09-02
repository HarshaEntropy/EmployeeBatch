package org.imaginnovate.repository;

import org.imaginnovate.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    List<PhoneNumber> findByEmployeeId(Long employeeId);
}