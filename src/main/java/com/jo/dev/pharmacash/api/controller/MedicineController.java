package com.jo.dev.pharmacash.api.controller;

import com.jo.dev.pharmacash.api.entity.Medicine;
import com.jo.dev.pharmacash.api.repository.MedicineRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineRepository repository;

    public MedicineController(MedicineRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Medicine> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> get(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        Medicine saved = repository.save(medicine);
        return ResponseEntity.created(URI.create("/api/medicines/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(@PathVariable Long id, @RequestBody Medicine payload) {
        return repository.findById(id).map(existing -> {
            existing.setName(payload.getName());
            existing.setDosage(payload.getDosage());
            existing.setType(payload.getType());
            existing.setExpiryDate(payload.getExpiryDate());
            existing.setPrice(payload.getPrice());
            Medicine saved = repository.save(existing);
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

