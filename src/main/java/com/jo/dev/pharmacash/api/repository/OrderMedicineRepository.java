package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.OrderMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMedicineRepository extends JpaRepository<OrderMedicine, Long> {
}

