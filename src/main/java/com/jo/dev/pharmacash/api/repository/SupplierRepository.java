package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}

