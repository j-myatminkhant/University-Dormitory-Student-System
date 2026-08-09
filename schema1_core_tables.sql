-- =============================================
-- SCHEMA 1: CORE TABLES
-- =============================================

-- Create the database
CREATE DATABASE IF NOT EXISTS hostel_management;
USE hostel_management;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS student_fee;
DROP TABLE IF EXISTS employee_fee;
DROP TABLE IF EXISTS leaved_students;
DROP TABLE IF EXISTS leaved_employees;
DROP TABLE IF EXISTS deleted_students;
DROP TABLE IF EXISTS deleted_employees;
DROP TABLE IF EXISTS register_students;
DROP TABLE IF EXISTS register_employee;

-- Table for currently living students
CREATE TABLE register_students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    nsbmID VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    nic VARCHAR(20) UNIQUE NOT NULL,
    address TEXT NOT NULL,
    guardName VARCHAR(100) NOT NULL,
    guardTel VARCHAR(20) NOT NULL,
    room INT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table for currently employed staff
CREATE TABLE register_employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    emp_id VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    nic VARCHAR(20) UNIQUE NOT NULL,
    tel VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    emg_tel VARCHAR(20) NOT NULL DEFAULT '+94-71-000-0000',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table for students who have left the hostel
CREATE TABLE leaved_students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    nsbmID VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    guardName VARCHAR(100) NOT NULL,
    guardTel VARCHAR(20) NOT NULL,
    leave_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for employees who have left
CREATE TABLE leaved_employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    emp_id VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    tel VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    leave_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for deleted students (for undo functionality)
CREATE TABLE deleted_students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    original_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    nsbmID VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    guardName VARCHAR(100) NOT NULL,
    guardTel VARCHAR(20) NOT NULL,
    room INT DEFAULT NULL,
    deleted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_by VARCHAR(100) DEFAULT 'SYSTEM'
);

-- Table for deleted employees (for undo functionality)
CREATE TABLE deleted_employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    original_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    emp_id VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    tel VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    deleted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_by VARCHAR(100) DEFAULT 'SYSTEM'
);

