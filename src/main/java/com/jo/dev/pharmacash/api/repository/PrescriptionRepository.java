package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}

