#!/bin/bash

echo "========================================"
echo "Hostel Management System Database Setup"
echo "========================================"
echo

echo "This script will help you set up the database for the Hostel Management System."
echo
echo "Prerequisites:"
echo "- MySQL Server must be installed and running"
echo "- MySQL command line client must be accessible"
echo

read -p "Press Enter to continue..."

echo
echo "Step 1: Testing MySQL connection..."

# Check if MySQL is available
if ! command -v mysql &> /dev/null; then
    echo "ERROR: MySQL command line client not found!"
    echo "Please ensure MySQL is installed and added to PATH"
    echo
    read -p "Press Enter to exit..."
    exit 1
fi

echo "MySQL found successfully!"
echo

echo "Step 2: Creating database and tables..."
echo "Please enter your MySQL root password (or press Enter if no password):"
read -s mysql_password

if [ -z "$mysql_password" ]; then
    mysql -u root -e "source database_schema.sql"
else
    mysql -u root -p"$mysql_password" -e "source database_schema.sql"
fi

if [ $? -eq 0 ]; then
    echo
    echo "SUCCESS: Database setup completed!"
    echo
    echo "You can now run the Hostel Management System application."
    echo
    echo "To test the database connection, run:"
    echo "java -cp 'target/classes:src/main/java/jar/*' DBConnection.DatabaseTest"
    echo
else
    echo
    echo "ERROR: Database setup failed!"
    echo "Please check the error messages above and ensure:"
    echo "- MySQL service is running"
    echo "- Root password is correct"
    echo "- You have sufficient privileges"
    echo
fi

echo "Press Enter to exit..."
read
