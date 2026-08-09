@echo off
echo ========================================
echo Hostel Management System Database Setup
echo ========================================
echo.

echo This script will help you set up the database for the Hostel Management System.
echo.
echo Prerequisites:
echo - MySQL Server must be installed and running
echo - MySQL command line client must be accessible
echo.

echo Press any key to continue...
pause >nul

echo.
echo Step 1: Testing MySQL connection...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: MySQL command line client not found!
    echo Please ensure MySQL is installed and added to PATH
    echo.
    pause
    exit /b 1
)

echo MySQL found successfully!
echo.

echo Step 2: Creating database and tables...
echo Please enter your MySQL root password (or press Enter if no password):
set /p mysql_password=

if "%mysql_password%"=="" (
    mysql -u root -e "source database_schema.sql"
) else (
    mysql -u root -p%mysql_password% -e "source database_schema.sql"
)

if %errorlevel% equ 0 (
    echo.
    echo SUCCESS: Database setup completed!
    echo.
    echo You can now run the Hostel Management System application.
    echo.
    echo To test the database connection, run:
    echo java -cp "target/classes;src/main/java/jar/*" DBConnection.DatabaseTest
    echo.
) else (
    echo.
    echo ERROR: Database setup failed!
    echo Please check the error messages above and ensure:
    echo - MySQL service is running
    echo - Root password is correct
    echo - You have sufficient privileges
    echo.
)

echo Press any key to exit...
pause >nul
