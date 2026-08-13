package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}

