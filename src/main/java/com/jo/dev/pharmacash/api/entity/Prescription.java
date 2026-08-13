package com.jo.dev.pharmacash.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDate prescribedAt;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionMedicine> items = new ArrayList<>();

    public Prescription() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getPrescribedAt() {
        return prescribedAt;
    }

    public void setPrescribedAt(LocalDate prescribedAt) {
        this.prescribedAt = prescribedAt;
    }

    public List<PrescriptionMedicine> getItems() {
        return items;
    }

    public void setItems(List<PrescriptionMedicine> items) {
        this.items = items;
    }
}

