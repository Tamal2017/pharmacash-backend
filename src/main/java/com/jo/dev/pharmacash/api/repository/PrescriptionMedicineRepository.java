package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.PrescriptionMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicine, Long> {
}

