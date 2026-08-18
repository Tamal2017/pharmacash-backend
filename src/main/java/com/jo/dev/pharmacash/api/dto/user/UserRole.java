package com.jo.dev.pharmacash.api.dto.user;

public enum UserRole {
    // Clinical Roles
    PHARMACIST("Pharmacist"),
    PHARMACY_TECHNICIAN("Pharmacy Technician"),
    PHARMACY_ASSISTANT("Pharmacy Assistant"),

    // Inventory & Supply Chain
    INVENTORY_MANAGER("Inventory Manager"),
    SUPPLIER_MANAGER("Supplier Manager"),

    // Sales & Billing
    CASHIER("Cashier / POS Operator"),
    BILLING_SPECIALIST("Billing Specialist"),

    // Audit
    AUDITOR("Auditor"),

    // Administration
    SYSTEM_ADMIN("System Administrator"),
    SUPER_ADMIN("Super Admin"),

    // Logistics
    DELIVERY_AGENT("Delivery Agent");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static UserRole fromValue(String label) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label cannot be null or empty");
        }

        for (UserRole role : values()) {
            if (role.label.equalsIgnoreCase(label.trim())) {
                return role;
            }
        }

        throw new IllegalArgumentException("Unknown role label: " + label);
    }
}
