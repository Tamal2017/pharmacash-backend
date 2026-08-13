package com.jo.dev.pharmacash.api.controller;

import com.jo.dev.pharmacash.api.entity.Pharmacy;
import com.jo.dev.pharmacash.api.repository.PharmacyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    private final PharmacyRepository repository;

    public PharmacyController(PharmacyRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Pharmacy> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> get(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pharmacy> create(@RequestBody Pharmacy pharmacy) {
        Pharmacy saved = repository.save(pharmacy);
        return ResponseEntity.created(URI.create("/api/pharmacies/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pharmacy> update(@PathVariable Long id, @RequestBody Pharmacy payload) {
        return repository.findById(id).map(existing -> {
            existing.setName(payload.getName());
            existing.setLocation(payload.getLocation());
            Pharmacy saved = repository.save(existing);
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

