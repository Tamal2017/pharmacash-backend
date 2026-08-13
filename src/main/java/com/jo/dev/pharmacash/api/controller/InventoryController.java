package com.jo.dev.pharmacash.api.controller;

import com.jo.dev.pharmacash.api.entity.Inventory;
import com.jo.dev.pharmacash.api.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository repository;

    public InventoryController(InventoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Inventory> list() {
        return repository.findAll();
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public List<Inventory> byPharmacy(@PathVariable Long pharmacyId) {
        return repository.findByPharmacyId(pharmacyId);
    }

    @GetMapping("/medicine/{medicineId}")
    public List<Inventory> byMedicine(@PathVariable Long medicineId) {
        return repository.findByMedicineId(medicineId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> get(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Inventory> create(@RequestBody Inventory inventory) {
        Inventory saved = repository.save(inventory);
        return ResponseEntity.created(URI.create("/api/inventory/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(@PathVariable Long id, @RequestBody Inventory payload) {
        return repository.findById(id).map(existing -> {
            existing.setQuantity(payload.getQuantity());
            Inventory saved = repository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repository.findById(id).map(existing -> {
            repository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

