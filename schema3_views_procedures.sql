-- =============================================
-- SCHEMA 3: VIEWS, PROCEDURES, AND INDEXES
-- =============================================

USE hostel_management;

-- Create views for easier data access
CREATE VIEW current_students AS
SELECT * FROM register_students ORDER BY name;

CREATE VIEW current_employees AS
SELECT * FROM register_employee ORDER BY name;

CREATE VIEW student_fee_summary AS
SELECT 
    s.id,
    s.name,
    s.nsbmID,
    COUNT(sf.id) as fee_records,
    SUM(sf.fee) as total_fees_paid
FROM register_students s
LEFT JOIN student_fee sf ON s.id = sf.studentid
GROUP BY s.id, s.name, s.nsbmID;

CREATE VIEW employee_salary_summary AS
SELECT 
    e.id,
    e.name,
    COUNT(ef.id) as salary_records,
    SUM(ef.salary) as total_salary_paid
FROM register_employee e
LEFT JOIN employee_fee ef ON e.id = ef.employeeid
GROUP BY e.id, e.name;

-- View for deleted employees
CREATE VIEW deleted_employees_view AS
SELECT 
    id,
    original_id,
    name,
    emp_id,
    email,
    nic,
    tel,
    address,
    department,
    position,
    deleted_date,
    deleted_by
FROM deleted_employees
ORDER BY deleted_date DESC;

-- View for deleted students
CREATE VIEW deleted_students_view AS
SELECT 
    id,
    original_id,
    name,
    nsbmID,
    email,
    phoneNumber,
    nic,
    address,
    guardName,
    guardTel,
    room,
    deleted_date,
    deleted_by
FROM deleted_students
ORDER BY deleted_date DESC;

-- Create indexes for better performance
CREATE INDEX idx_student_nsbm ON register_students(nsbmID);
CREATE INDEX idx_student_room ON register_students(room);
CREATE INDEX idx_student_nic ON register_students(nic);
CREATE INDEX idx_student_email ON register_students(email);
CREATE INDEX idx_employee_emp_id ON register_employee(emp_id);
CREATE INDEX idx_employee_email ON register_employee(email);
CREATE INDEX idx_employee_nic ON register_employee(nic);
CREATE INDEX idx_deleted_employee_original_id ON deleted_employees(original_id);
CREATE INDEX idx_deleted_student_original_id ON deleted_students(original_id);
CREATE INDEX idx_fee_year_month ON student_fee(year, month);
CREATE INDEX idx_salary_year_month ON employee_fee(year, month);

-- Stored procedures for delete/restore functionality

-- Procedure to delete an employee (move to deleted_employees table)
DELIMITER //
CREATE PROCEDURE DeleteEmployee(IN emp_id_param INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Insert into deleted_employees table
    INSERT INTO deleted_employees (original_id, name, emp_id, email, nic, tel, address, department, position)
    SELECT id, name, emp_id, email, nic, tel, address, department, position
    FROM register_employee 
    WHERE id = emp_id_param;
    
    -- Delete from register_employee table
    DELETE FROM register_employee WHERE id = emp_id_param;
    
    COMMIT;
END //
DELIMITER ;

-- Procedure to restore an employee (move back to register_employee table)
DELIMITER //
CREATE PROCEDURE RestoreEmployee(IN deleted_emp_id INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Insert back into register_employee table
    INSERT INTO register_employee (id, name, emp_id, email, nic, tel, address, department, position, emg_tel)
    SELECT original_id, name, emp_id, email, nic, tel, address, department, position, '+94-71-000-0000'
    FROM deleted_employees 
    WHERE id = deleted_emp_id;
    
    -- Remove from deleted_employees table
    DELETE FROM deleted_employees WHERE id = deleted_emp_id;
    
    COMMIT;
END //
DELIMITER ;

-- Procedure to delete a student (move to deleted_students table)
DELIMITER //
CREATE PROCEDURE DeleteStudent(IN student_id_param INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Insert into deleted_students table
    INSERT INTO deleted_students (original_id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room)
    SELECT id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room
    FROM register_students 
    WHERE id = student_id_param;
    
    -- Delete from register_students table
    DELETE FROM register_students WHERE id = student_id_param;
    
    COMMIT;
END //
DELIMITER ;

-- Procedure to restore a student (move back to register_students table)
DELIMITER //
CREATE PROCEDURE RestoreStudent(IN deleted_student_id INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Insert back into register_students table
    INSERT INTO register_students (id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room)
    SELECT original_id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room
    FROM deleted_students 
    WHERE id = deleted_student_id;
    
    -- Remove from deleted_students table
    DELETE FROM deleted_students WHERE id = deleted_student_id;
    
    COMMIT;
END //
DELIMITER ;

-- Grant permissions (adjust as needed for your MySQL setup)
-- GRANT ALL PRIVILEGES ON hostel_management.* TO 'your_username'@'localhost';
-- FLUSH PRIVILEGES;

