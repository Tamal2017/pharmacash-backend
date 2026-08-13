package com.jo.dev.pharmacash.api.repository;

import com.jo.dev.pharmacash.api.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}

