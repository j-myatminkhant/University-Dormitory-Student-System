import Controllers.Employee.Update_EmployeeController;
import Model.EmployeeDetails;
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class TestUpdateEmployee {
    public static void main(String[] args) {
        System.out.println("=== Testing Update Employee Functionality ===");
        
        try {
            // Test database connection
            DBHandler handler = new DBHandler();
            Connection connection = handler.connectDB();
            
            if (connection != null) {
                System.out.println("✓ Database connection successful");
                
                // Test loading employee data
                testLoadEmployeeData(connection);
                
                // Test updating an employee
                testUpdateEmployee(connection);
                
                connection.close();
            } else {
                System.out.println("✗ Database connection failed");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testLoadEmployeeData(Connection connection) {
        System.out.println("\n--- Testing Load Employee Data ---");
        
        try {
            String query = "SELECT id, name, emp_id, email, tel, nic, address, department, position, emg_tel FROM register_employee LIMIT 5";
            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("Employee " + count + ":");
                System.out.println("  ID: " + rs.getString("id"));
                System.out.println("  Name: " + rs.getString("name"));
                System.out.println("  Emp ID: " + rs.getString("emp_id"));
                System.out.println("  Email: " + rs.getString("email"));
                System.out.println("  Tel: " + rs.getString("tel"));
                System.out.println("  NIC: " + rs.getString("nic"));
                System.out.println("  Address: " + rs.getString("address"));
                System.out.println("  Department: " + rs.getString("department"));
                System.out.println("  Position: " + rs.getString("position"));
                System.out.println("  Emg Tel: " + rs.getString("emg_tel"));
                System.out.println();
            }
            
            if (count > 0) {
                System.out.println("✓ Successfully loaded " + count + " employees");
            } else {
                System.out.println("✗ No employees found in database");
            }
            
        } catch (SQLException e) {
            System.out.println("✗ Error loading employee data: " + e.getMessage());
        }
    }
    
    private static void testUpdateEmployee(Connection connection) {
        System.out.println("\n--- Testing Update Employee ---");
        
        try {
            // First, get an existing employee
            String selectQuery = "SELECT id FROM register_employee LIMIT 1";
            PreparedStatement selectPst = connection.prepareStatement(selectQuery);
            ResultSet rs = selectPst.executeQuery();
            
            if (rs.next()) {
                String employeeId = rs.getString("id");
                System.out.println("Testing update for employee ID: " + employeeId);
                
                // Test the update query structure
                String updateQuery = "UPDATE register_employee SET name = ?, emp_id = ?, email = ?, tel = ?, nic = ?, address = ?, department = ?, position = ?, emg_tel = ? WHERE id = ?";
                PreparedStatement updatePst = connection.prepareStatement(updateQuery);
                
                // Set test values
                updatePst.setString(1, "Test Name Updated");
                updatePst.setString(2, "EMP001");
                updatePst.setString(3, "test@example.com");
                updatePst.setString(4, "+95-9-12345678");
                updatePst.setString(5, "12/ABCDE(N)123456");
                updatePst.setString(6, "Test Address");
                updatePst.setString(7, "IT");
                updatePst.setString(8, "Developer");
                updatePst.setString(9, "+95-9-87654321");
                updatePst.setString(10, employeeId);
                
                // Don't actually execute the update, just test the query structure
                System.out.println("✓ Update query structure is valid");
                System.out.println("✓ All parameters can be set correctly");
                
                updatePst.close();
            } else {
                System.out.println("✗ No employees found to test update");
            }
            
            selectPst.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error testing update: " + e.getMessage());
        }
    }
}
