-- Create Pharmacy table
CREATE TABLE pharmacies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- Create Supplier table
CREATE TABLE suppliers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255),
    address VARCHAR(255)
);

-- Create Medicine table
CREATE TABLE medicines (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255),
    type VARCHAR(255),
    expiry_date DATE,
    price DECIMAL(10, 2),
    supplier_id BIGINT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

-- Create Inventory table
CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,
    pharmacy_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    quantity INT,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id),
    UNIQUE (pharmacy_id, medicine_id)
);

-- Create Employee table
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    pharmacy_id BIGINT,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies(id)
);

-- Create Customer table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255),
    allergies VARCHAR(1000)
);

-- Create Purchase Order table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT,
    order_date DATE,
    total DECIMAL(10, 2),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

-- Create Order Medicine table (line items)
CREATE TABLE order_medicine (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    quantity INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);

-- Create Prescription table
CREATE TABLE prescriptions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    prescribed_at DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Create Prescription Medicine table (line items)
CREATE TABLE prescription_medicine (
    id BIGSERIAL PRIMARY KEY,
    prescription_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    quantity INT,
    directions VARCHAR(1000),
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);

-- Create Sales Transaction table
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT,
    medicine_id BIGINT,
    pharmacy_id BIGINT,
    quantity INT,
    date TIMESTAMP,
    total_price DECIMAL(10, 2),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id),
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacies(id)
);

-- Create indexes for foreign keys and common queries
CREATE INDEX idx_medicines_supplier ON medicines(supplier_id);
CREATE INDEX idx_inventory_pharmacy ON inventory(pharmacy_id);
CREATE INDEX idx_inventory_medicine ON inventory(medicine_id);
CREATE INDEX idx_employees_pharmacy ON employees(pharmacy_id);
CREATE INDEX idx_orders_supplier ON orders(supplier_id);
CREATE INDEX idx_order_medicine_order ON order_medicine(order_id);
CREATE INDEX idx_order_medicine_medicine ON order_medicine(medicine_id);
CREATE INDEX idx_prescriptions_customer ON prescriptions(customer_id);
CREATE INDEX idx_prescription_medicine_prescription ON prescription_medicine(prescription_id);
CREATE INDEX idx_prescription_medicine_medicine ON prescription_medicine(medicine_id);
CREATE INDEX idx_transactions_customer ON transactions(customer_id);
CREATE INDEX idx_transactions_medicine ON transactions(medicine_id);
CREATE INDEX idx_transactions_pharmacy ON transactions(pharmacy_id);
CREATE INDEX idx_transactions_date ON transactions(date);

