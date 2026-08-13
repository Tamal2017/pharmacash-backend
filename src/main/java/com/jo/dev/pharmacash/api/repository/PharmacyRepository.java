package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}

