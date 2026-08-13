package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

