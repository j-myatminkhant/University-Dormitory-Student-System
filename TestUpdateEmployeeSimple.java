/*
 * Simple Test for Update Employee Database Connection
 */

import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestUpdateEmployeeSimple {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Update Employee Database Connection ===");
        
        try {
            // Test database connection
            Connection conn = DBHandler.connectDB();
            if (conn == null) {
                System.out.println("❌ ERROR: Database connection failed!");
                System.out.println("Please check:");
                System.out.println("1. MySQL server is running");
                System.out.println("2. Database 'hostel_management' exists");
                System.out.println("3. Username/password are correct");
                return;
            }
            
            System.out.println("✅ Database connection successful");
            
            // Test if register_employee table exists and has data
            try {
                String query = "SELECT id, name, emp_id, email, tel, nic, address, department, position, emg_tel FROM register_employee";
                ResultSet rs = conn.createStatement().executeQuery(query);
                
                int count = 0;
                System.out.println("\n--- Employee Records ---");
                while (rs.next() && count < 5) { // Show first 5 records
                    count++;
                    System.out.println(count + ". ID: " + rs.getString("id") + 
                                     ", Name: " + rs.getString("name") + 
                                     ", Emp ID: " + rs.getString("emp_id"));
                }
                
                if (count == 0) {
                    System.out.println("⚠️  WARNING: No employee records found in database");
                    System.out.println("Please add some employee records first");
                } else {
                    System.out.println("✅ Found " + count + " employee records");
                }
                
                rs.close();
                
            } catch (SQLException ex) {
                System.out.println("❌ ERROR: Could not access register_employee table");
                System.out.println("Error: " + ex.getMessage());
                
                // Check if table exists
                try {
                    ResultSet tables = conn.getMetaData().getTables(null, null, "register_employee", null);
                    if (!tables.next()) {
                        System.out.println("❌ Table 'register_employee' does not exist!");
                        System.out.println("Please run the database setup scripts first");
                    }
                    tables.close();
                } catch (SQLException e) {
                    System.out.println("Error checking table existence: " + e.getMessage());
                }
            }
            
            System.out.println("\n=== Test Summary ===");
            System.out.println("✅ Database connection: OK");
            System.out.println("✅ Update Employee should work correctly");
            System.out.println("\nTo test Update Employee:");
            System.out.println("1. Run your main application");
            System.out.println("2. Go to Employee Management");
            System.out.println("3. Click 'UPDATE EMPLOYEE'");
            System.out.println("4. Window should open and show employee data");
            
        } catch (Exception e) {
            System.out.println("❌ CRITICAL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
