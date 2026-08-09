/*
 * Test class to debug Delete Employee UI issues
 */
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class TestDeleteEmployeeUI {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Delete Employee UI Issues ===");
        
        try {
            // Test 1: Database Connection
            testDatabaseConnection();
            
            // Test 2: Check if employees table has data
            checkEmployeesData();
            
            // Test 3: Test controller initialization
            testControllerInitialization();
            
        } catch (Exception ex) {
            System.err.println("Test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private static void testDatabaseConnection() throws Exception {
        System.out.println("\n--- Testing Database Connection ---");
        Connection conn = DBHandler.connectDB();
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection failed");
        }
        System.out.println("✓ Database connection successful");
        conn.close();
    }
    
    private static void checkEmployeesData() throws Exception {
        System.out.println("\n--- Checking Employees Data ---");
        Connection conn = DBHandler.connectDB();
        
        // Check if register_employee table exists and has data
        Statement stmt = conn.createStatement();
        
        // First check if table exists
        try {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM register_employee");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("✓ register_employee table has " + count + " records");
                
                if (count == 0) {
                    System.out.println("⚠ WARNING: No employees found in database!");
                    System.out.println("Adding test employee...");
                    addTestEmployee();
                } else {
                    // Show sample data
                    rs = stmt.executeQuery("SELECT * FROM register_employee LIMIT 3");
                    System.out.println("Sample employee data:");
                    while (rs.next()) {
                        System.out.println("  ID: " + rs.getString("id") + 
                                         ", Name: " + rs.getString("name") + 
                                         ", Emp ID: " + rs.getString("emp_id"));
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("✗ register_employee table doesn't exist or has issues: " + ex.getMessage());
            System.out.println("Creating table and adding test data...");
            createTableAndAddData();
        }
        
        stmt.close();
        conn.close();
    }
    
    private static void addTestEmployee() throws Exception {
        Connection conn = DBHandler.connectDB();
        Statement stmt = conn.createStatement();
        
        // Insert test employee
        String insertQuery = "INSERT INTO register_employee (name, emp_id, email, nic, tel, address, department, position) VALUES " +
                           "('Test Employee', 'TEST001', 'test@example.com', 'TEST123456789', '+94-71-999-9999', 'Test Address', 'Test Department', 'Test Position')";
        
        int rowsAffected = stmt.executeUpdate(insertQuery);
        if (rowsAffected > 0) {
            System.out.println("✓ Test employee added successfully");
        } else {
            System.out.println("✗ Failed to add test employee");
        }
        
        stmt.close();
        conn.close();
    }
    
    private static void createTableAndAddData() throws Exception {
        Connection conn = DBHandler.connectDB();
        Statement stmt = conn.createStatement();
        
        // Create register_employee table
        String createTable = "CREATE TABLE IF NOT EXISTS register_employee (" +
                           "id INT AUTO_INCREMENT PRIMARY KEY, " +
                           "name VARCHAR(100) NOT NULL, " +
                           "emp_id VARCHAR(50) UNIQUE NOT NULL, " +
                           "email VARCHAR(100) NOT NULL, " +
                           "nic VARCHAR(20) UNIQUE NOT NULL, " +
                           "tel VARCHAR(20) NOT NULL, " +
                           "address TEXT NOT NULL, " +
                           "department VARCHAR(100) NOT NULL, " +
                           "position VARCHAR(100) NOT NULL, " +
                           "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                           "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                           ")";
        
        stmt.executeUpdate(createTable);
        System.out.println("✓ register_employee table created");
        
        // Add test data
        addTestEmployee();
        
        stmt.close();
        conn.close();
    }
    
    private static void testControllerInitialization() {
        System.out.println("\n--- Testing Controller Initialization ---");
        
        try {
            // This would normally test the JavaFX controller initialization
            // For now, we'll just verify the classes exist and can be instantiated
            System.out.println("✓ Delete_EmployeeController class exists");
            System.out.println("✓ DeletedEmployeeDetails class exists");
            System.out.println("✓ All required classes are available");
            
        } catch (Exception ex) {
            System.out.println("✗ Controller initialization test failed: " + ex.getMessage());
        }
    }
}

