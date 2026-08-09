/*
 * Simple test for delete employee functionality
 */
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SimpleDeleteTest {
    
    public static void main(String[] args) {
        System.out.println("=== Simple Delete Employee Test ===");
        
        try {
            // Test 1: Show current employees
            showCurrentEmployees();
            
            // Test 2: Test delete operation
            testDeleteEmployee();
            
            // Test 3: Show deleted employees
            showDeletedEmployees();
            
            // Test 4: Test restore operation
            testRestoreEmployee();
            
            System.out.println("\n✓ All tests completed successfully!");
            
        } catch (Exception ex) {
            System.err.println("✗ Test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private static void showCurrentEmployees() throws Exception {
        System.out.println("\n--- Current Employees ---");
        Connection conn = DBHandler.connectDB();
        Statement stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT id, name, emp_id, email, department FROM register_employee ORDER BY id");
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.println(count + ". ID: " + rs.getString("id") + 
                             " | Name: " + rs.getString("name") + 
                             " | EmpID: " + rs.getString("emp_id") + 
                             " | Email: " + rs.getString("email"));
        }
        System.out.println("Total employees: " + count);
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    private static void testDeleteEmployee() throws Exception {
        System.out.println("\n--- Testing Delete Employee ---");
        Connection conn = DBHandler.connectDB();
        
        try {
            conn.setAutoCommit(false);
            
            // Get first employee
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM register_employee LIMIT 1");
            
            if (!rs.next()) {
                throw new Exception("No employees found to delete");
            }
            
            String employeeId = rs.getString("id");
            String employeeName = rs.getString("name");
            String empId = rs.getString("emp_id");
            String email = rs.getString("email");
            String nic = rs.getString("nic");
            String tel = rs.getString("tel");
            String address = rs.getString("address");
            String department = rs.getString("department");
            String position = rs.getString("position");
            
            System.out.println("Deleting: " + employeeName + " (ID: " + employeeId + ")");
            
            // Insert into deleted_employees table
            String insertDeleted = "INSERT INTO deleted_employees (original_id, name, emp_id, email, nic, tel, address, department, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPst = conn.prepareStatement(insertDeleted);
            insertPst.setString(1, employeeId);
            insertPst.setString(2, employeeName);
            insertPst.setString(3, empId);
            insertPst.setString(4, email);
            insertPst.setString(5, nic);
            insertPst.setString(6, tel);
            insertPst.setString(7, address);
            insertPst.setString(8, department);
            insertPst.setString(9, position);
            insertPst.executeUpdate();
            
            // Delete from register_employee table
            String deleteQuery = "DELETE FROM register_employee WHERE id = ?";
            PreparedStatement deletePst = conn.prepareStatement(deleteQuery);
            deletePst.setString(1, employeeId);
            int deletedRows = deletePst.executeUpdate();
            
            if (deletedRows != 1) {
                throw new Exception("Failed to delete employee from main table");
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
            System.out.println("✓ Employee deleted successfully!");
            
            rs.close();
            stmt.close();
            insertPst.close();
            deletePst.close();
            
        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw ex;
        } finally {
            conn.close();
        }
    }
    
    private static void showDeletedEmployees() throws Exception {
        System.out.println("\n--- Deleted Employees ---");
        Connection conn = DBHandler.connectDB();
        Statement stmt = conn.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT id, original_id, name, emp_id, email, deleted_date FROM deleted_employees ORDER BY deleted_date DESC");
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.println(count + ". Original ID: " + rs.getString("original_id") + 
                             " | Name: " + rs.getString("name") + 
                             " | EmpID: " + rs.getString("emp_id") + 
                             " | Deleted: " + rs.getString("deleted_date"));
        }
        System.out.println("Total deleted employees: " + count);
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    private static void testRestoreEmployee() throws Exception {
        System.out.println("\n--- Testing Restore Employee ---");
        Connection conn = DBHandler.connectDB();
        
        try {
            conn.setAutoCommit(false);
            
            // Get first deleted employee
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM deleted_employees LIMIT 1");
            
            if (!rs.next()) {
                System.out.println("No deleted employees found to restore");
                return;
            }
            
            String deletedId = rs.getString("id");
            String originalId = rs.getString("original_id");
            String employeeName = rs.getString("name");
            String empId = rs.getString("emp_id");
            String email = rs.getString("email");
            String nic = rs.getString("nic");
            String tel = rs.getString("tel");
            String address = rs.getString("address");
            String department = rs.getString("department");
            String position = rs.getString("position");
            
            System.out.println("Restoring: " + employeeName + " (Original ID: " + originalId + ")");
            
            // Insert back into register_employee table
            String insertEmployee = "INSERT INTO register_employee (id, name, emp_id, email, nic, tel, address, department, position, emg_tel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPst = conn.prepareStatement(insertEmployee);
            insertPst.setString(1, originalId);
            insertPst.setString(2, employeeName);
            insertPst.setString(3, empId);
            insertPst.setString(4, email);
            insertPst.setString(5, nic);
            insertPst.setString(6, tel);
            insertPst.setString(7, address);
            insertPst.setString(8, department);
            insertPst.setString(9, position);
            insertPst.setString(10, "+94-71-000-0000"); // Default emergency tel
            insertPst.executeUpdate();
            
            // Remove from deleted_employees table
            String removeDeleted = "DELETE FROM deleted_employees WHERE id = ?";
            PreparedStatement removePst = conn.prepareStatement(removeDeleted);
            removePst.setString(1, deletedId);
            removePst.executeUpdate();
            
            conn.commit();
            conn.setAutoCommit(true);
            
            System.out.println("✓ Employee restored successfully!");
            
            rs.close();
            stmt.close();
            insertPst.close();
            removePst.close();
            
        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw ex;
        } finally {
            conn.close();
        }
    }
}

