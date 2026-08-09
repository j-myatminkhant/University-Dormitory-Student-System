# Hostel Management System - Database Setup Guide

## Overview
This document provides step-by-step instructions for setting up the database for the Hostel Management System JavaFX application.

## Prerequisites
- MySQL Server 8.0 or higher
- MySQL Workbench (optional, for GUI management)
- Java 8 or higher
- Maven (for dependency management)

## Database Setup

### 1. Install MySQL Server
- Download and install MySQL Server from [MySQL Official Website](https://dev.mysql.com/downloads/mysql/)
- During installation, set a root password (or leave blank for development)
- Ensure MySQL service is running

### 2. Create Database and Tables
1. Open MySQL command line client or MySQL Workbench
2. Run the provided `database_schema.sql` file:
   ```sql
   source /path/to/database_schema.sql;
   ```
   
   Or copy and paste the contents of the SQL file directly.

### 3. Verify Database Creation
```sql
SHOW DATABASES;
USE hostel_management;
SHOW TABLES;
```

You should see the following tables:
- `register_students` - Currently living students
- `register_employee` - Currently employed staff
- `leaved_students` - Students who have left
- `leaved_employees` - Employees who have left
- `student_fee` - Student fee records
- `employee_fee` - Employee salary records

## Database Schema Details

### Table: register_students
Stores information about currently living students:
- `id` - Auto-increment primary key
- `name` - Student's full name
- `nsbmID` - Unique NSBM student ID
- `email` - Student's email address
- `phoneNumber` - Student's phone number
- `nic` - National Identity Card number
- `address` - Student's address
- `guardName` - Guardian's name
- `guardTel` - Guardian's telephone number
- `created_at` - Record creation timestamp
- `updated_at` - Last update timestamp

### Table: register_employee
Stores information about currently employed staff:
- `id` - Auto-increment primary key
- `name` - Employee's full name
- `nic` - National Identity Card number
- `tel` - Employee's telephone number
- `emg_tel` - Emergency contact number
- `created_at` - Record creation timestamp
- `updated_at` - Last update timestamp

### Table: student_fee
Stores student fee payment records:
- `id` - Auto-increment primary key
- `studentid` - Foreign key to register_students
- `year` - Academic year
- `fee` - Fee amount
- `month` - Month of payment
- `created_at` - Record creation timestamp

### Table: employee_fee
Stores employee salary records:
- `id` - Auto-increment primary key
- `employeeid` - Foreign key to register_employee
- `year` - Year
- `salary` - Salary amount
- `month` - Month of salary
- `created_at` - Record creation timestamp

## Application Configuration

### 1. Database Connection Settings
The application uses the following default database settings:
- **Host**: localhost
- **Port**: 3306
- **Database**: hostel_management
- **Username**: root
- **Password**: (blank)

### 2. Update Connection Settings (if needed)
Edit `src/main/java/DBConnection/DBHandler.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/hostel_management";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";
```

### 3. Test Database Connection
Run the application and check the console for connection messages:
```
Database connected successfully!
```

## Sample Data
The database schema includes sample data for testing:
- 3 sample students
- 3 sample employees
- Sample fee and salary records

## Database Views
The following views are created for easier data access:
- `current_students` - All currently living students
- `current_employees` - All currently employed staff
- `student_fee_summary` - Student fee summary with totals
- `employee_salary_summary` - Employee salary summary with totals

## Troubleshooting

### Common Issues

#### 1. Connection Refused
- Ensure MySQL service is running
- Check if MySQL is listening on port 3306
- Verify firewall settings

#### 2. Access Denied
- Check username and password
- Ensure user has privileges on hostel_management database
- Try connecting with MySQL command line client

#### 3. Driver Not Found
- Ensure MySQL Connector/J is in the classpath
- Check Maven dependencies in pom.xml
- Verify jar files in src/main/java/jar/ directory

#### 4. Table Not Found
- Verify database_schema.sql was executed successfully
- Check if database name is correct
- Ensure all tables were created

### Useful MySQL Commands
```sql
-- Check MySQL version
SELECT VERSION();

-- Check database status
SHOW DATABASES;
USE hostel_management;
SHOW TABLES;

-- Check table structure
DESCRIBE register_students;
DESCRIBE register_employee;

-- Check sample data
SELECT * FROM register_students LIMIT 5;
SELECT * FROM register_employee LIMIT 5;

-- Check foreign key constraints
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE REFERENCED_TABLE_SCHEMA = 'hostel_management';
```

## Performance Optimization
- Indexes are created on frequently queried columns
- Foreign key constraints ensure data integrity
- Views provide optimized data access patterns

## Backup and Recovery
```sql
-- Create backup
mysqldump -u root -p hostel_management > backup.sql

-- Restore from backup
mysql -u root -p hostel_management < backup.sql
```

## Security Considerations
- Change default root password in production
- Create dedicated database user with minimal privileges
- Enable SSL connections for production environments
- Regular database backups
- Monitor database access logs

## Support
For database-related issues:
1. Check MySQL error logs
2. Verify connection parameters
3. Test with MySQL command line client
4. Review application console output

## Version Information
- Database Schema Version: 1.0
- Compatible with MySQL 8.0+
- Last Updated: 2024
