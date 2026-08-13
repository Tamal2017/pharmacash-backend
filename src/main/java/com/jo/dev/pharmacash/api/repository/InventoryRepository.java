package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByPharmacyId(Long pharmacyId);
    List<Inventory> findByMedicineId(Long medicineId);
}

