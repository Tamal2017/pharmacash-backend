package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.SaleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleTransactionRepository extends JpaRepository<SaleTransaction, Long> {
}

