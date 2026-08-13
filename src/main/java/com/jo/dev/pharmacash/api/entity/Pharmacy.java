package com.jo.dev.pharmacash.api.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pharmacies")
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventoryEntries = new ArrayList<>();

    public Pharmacy() {}

    public Pharmacy(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Inventory> getInventoryEntries() {
        return inventoryEntries;
    }

    public void setInventoryEntries(List<Inventory> inventoryEntries) {
        this.inventoryEntries = inventoryEntries;
    }
}

