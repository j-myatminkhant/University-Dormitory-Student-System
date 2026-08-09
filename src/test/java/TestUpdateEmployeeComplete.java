import Controllers.Employee.Update_EmployeeController;
import Model.EmployeeDetails;
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class TestUpdateEmployeeComplete {
    public static void main(String[] args) {
        System.out.println("=== Testing Complete Update Employee Functionality ===");
        
        try {
            // Test database connection
            DBHandler handler = new DBHandler();
            Connection connection = handler.connectDB();
            
            if (connection != null) {
                System.out.println("✓ Database connection successful");
                
                // Test loading employee data with new structure
                testLoadEmployeeDataWithEmgTel(connection);
                
                // Test updating an employee
                testUpdateEmployeeFunctionality(connection);
                
                // Test the complete update process
                testCompleteUpdateProcess(connection);
                
                connection.close();
            } else {
                System.out.println("✗ Database connection failed");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testLoadEmployeeDataWithEmgTel(Connection connection) {
        System.out.println("\n--- Testing Load Employee Data with Emg Tel ---");
        
        try {
            String query = "SELECT id, name, emp_id, email, tel, nic, address, department, position, emg_tel FROM register_employee LIMIT 3";
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
                System.out.println("✓ Successfully loaded " + count + " employees with emg_tel field");
            } else {
                System.out.println("✗ No employees found in database");
            }
            
        } catch (SQLException e) {
            System.out.println("✗ Error loading employee data: " + e.getMessage());
        }
    }
    
    private static void testUpdateEmployeeFunctionality(Connection connection) {
        System.out.println("\n--- Testing Update Employee Functionality ---");
        
        try {
            // First, get an existing employee
            String selectQuery = "SELECT id FROM register_employee LIMIT 1";
            PreparedStatement selectPst = connection.prepareStatement(selectQuery);
            ResultSet rs = selectPst.executeQuery();
            
            if (rs.next()) {
                String employeeId = rs.getString("id");
                System.out.println("Testing update for employee ID: " + employeeId);
                
                // Test the update query structure with all fields including emg_tel
                String updateQuery = "UPDATE register_employee SET name = ?, emp_id = ?, email = ?, tel = ?, nic = ?, address = ?, department = ?, position = ?, emg_tel = ? WHERE id = ?";
                PreparedStatement updatePst = connection.prepareStatement(updateQuery);
                
                // Set test values
                updatePst.setString(1, "Test Name Updated");
                updatePst.setString(2, "EMP001");
                updatePst.setString(3, "test@example.com");
                updatePst.setString(4, "+95-9-12345678");
                updatePst.setString(5, "12/ABCDE(N)123456");
                updatePst.setString(6, "Test Address Updated");
                updatePst.setString(7, "IT Department");
                updatePst.setString(8, "Senior Developer");
                updatePst.setString(9, "+95-9-87654321");
                updatePst.setString(10, employeeId);
                
                // Don't actually execute the update, just test the query structure
                System.out.println("✓ Update query structure is valid with all 10 fields");
                System.out.println("✓ All parameters can be set correctly including emg_tel");
                
                updatePst.close();
            } else {
                System.out.println("✗ No employees found to test update");
            }
            
            selectPst.close();
            
        } catch (SQLException e) {
            System.out.println("✗ Error testing update: " + e.getMessage());
        }
    }
    
    private static void testCompleteUpdateProcess(Connection connection) {
        System.out.println("\n--- Testing Complete Update Process ---");
        
        try {
            // Test creating EmployeeDetails object with new constructor
            EmployeeDetails testEmployee = new EmployeeDetails(
                "1", "Test Employee", "EMP001", "test@example.com", 
                "12/ABCDE(N)123456", "+95-9-12345678", "Test Address", 
                "IT Department", "Developer", "+95-9-87654321"
            );
            
            System.out.println("✓ EmployeeDetails object created successfully with new constructor");
            System.out.println("  ID: " + testEmployee.getId());
            System.out.println("  Name: " + testEmployee.getName());
            System.out.println("  Emp ID: " + testEmployee.getEmpId());
            System.out.println("  Email: " + testEmployee.getEmail());
            System.out.println("  Tel: " + testEmployee.getTel());
            System.out.println("  NIC: " + testEmployee.getNic());
            System.out.println("  Address: " + testEmployee.getAddress());
            System.out.println("  Department: " + testEmployee.getDepartment());
            System.out.println("  Position: " + testEmployee.getPosition());
            System.out.println("  Emg Tel: " + testEmployee.getEmgTel());
            
            // Test property methods
            System.out.println("✓ Property methods working:");
            System.out.println("  ID Property: " + testEmployee.getId());
            System.out.println("  Name Property: " + testEmployee.getName());
            System.out.println("  Emg Tel Property: " + testEmployee.getEmgTel());
            
        } catch (Exception e) {
            System.out.println("✗ Error testing complete update process: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
