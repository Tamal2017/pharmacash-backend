package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

