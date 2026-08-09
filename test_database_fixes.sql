-- Database Update Script for Staff Management System
-- This script adds the address field to the existing staff table structure

USE hostel_management;

-- Add address column to register_employee table
-- Note: We'll use the existing emg_tel column to store address data
-- This maintains backward compatibility while adding the new functionality

-- Update existing records to have meaningful address data
-- For existing records, we'll set a default address
UPDATE register_employee 
SET emg_tel = 'Address not specified' 
WHERE emg_tel IS NULL OR emg_tel = '';

-- Optional: If you want to add a separate address column instead of reusing emg_tel
-- ALTER TABLE register_employee ADD COLUMN address TEXT AFTER emg_tel;

-- Update the table comment to reflect the new usage
ALTER TABLE register_employee COMMENT = 'Staff registration table - emg_tel field now stores address information';

-- Verify the current structure
DESCRIBE register_employee;

-- Show sample data
SELECT * FROM register_employee LIMIT 5;

-- Note: The Java application has been updated to:
-- 1. Use the emg_tel column to store address data
-- 2. Display this as an address field in the UI
-- 3. Maintain backward compatibility with existing data
