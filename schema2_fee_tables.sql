-- =============================================
-- SCHEMA 2: FEE AND PAYMENT TABLES
-- =============================================

USE hostel_management;

-- Table for student fee records
CREATE TABLE student_fee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    studentid INT NOT NULL,
    year VARCHAR(4) NOT NULL,
    fee DECIMAL(10,2) NOT NULL,
    month VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (studentid) REFERENCES register_students(id) ON DELETE CASCADE,
    INDEX idx_student_year_month (studentid, year, month)
);

-- Table for employee salary records
CREATE TABLE employee_fee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employeeid INT NOT NULL,
    year VARCHAR(4) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    month VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employeeid) REFERENCES register_employee(id) ON DELETE CASCADE,
    INDEX idx_employee_year_month (employeeid, year, month)
);

-- Insert sample data for testing

-- Sample students
INSERT INTO register_students (name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel) VALUES
('John Doe', 'NSBM001', 'john.doe@nsbm.lk', '+94-71-123-4567', 'NIC001', '123 Main St, Colombo', 'Jane Doe', '+94-71-123-4568'),
('Jane Smith', 'NSBM002', 'jane.smith@nsbm.lk', '+94-71-234-5678', 'NIC002', '456 Oak Ave, Kandy', 'John Smith', '+94-71-234-5679'),
('Mike Johnson', 'NSBM003', 'mike.johnson@nsbm.lk', '+94-71-345-6789', 'NIC003', '789 Pine Rd, Galle', 'Sarah Johnson', '+94-71-345-6790');

-- Sample employees
INSERT INTO register_employee (name, emp_id, email, nic, tel, address, department, position, emg_tel) VALUES
('Admin User', 'EMP001', 'admin@gmail.com', 'NIC101', '+94-71-111-1111', '123 Admin St, Colombo', 'Administration', 'Administrator', '+94-71-111-1112'),
('Security Guard', 'EMP002', 'security@gmail.com', 'NIC102', '+94-71-222-2222', '456 Security Ave, Kandy', 'Security', 'Security Officer', '+94-71-222-2223'),
('Maintenance Staff', 'EMP003', 'maintenance@gmail.com', 'NIC103', '+94-71-333-3333', '789 Maintenance Rd, Galle', 'Maintenance', 'Maintenance Worker', '+94-71-333-3334');

-- Sample fee records
INSERT INTO student_fee (studentid, year, fee, month) VALUES
(1, '2024', 25000.00, 'January'),
(1, '2024', 25000.00, 'February'),
(2, '2024', 25000.00, 'January'),
(3, '2024', 25000.00, 'January');

-- Sample salary records
INSERT INTO employee_fee (employeeid, year, salary, month) VALUES
(1, '2024', 50000.00, 'January'),
(1, '2024', 50000.00, 'February'),
(2, '2024', 35000.00, 'January'),
(3, '2024', 40000.00, 'January');

