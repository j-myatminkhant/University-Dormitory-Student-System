/*
 * Simple test to verify delete employee functionality
 */
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDeleteFunctionality {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Delete Employee Functionality ===");
        
        try {
            // Test 1: Check if we can load employees
            testLoadEmployees();
            
            // Test 2: Test delete operation
            testDeleteOperation();
            
            // Test 3: Test restore operation
            testRestoreOperation();
            
            System.out.println("\n✓ All tests completed successfully!");
            
        } catch (Exception ex) {
            System.err.println("✗ Test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private static void testLoadEmployees() throws Exception {
        System.out.println("\n--- Testing Load Employees ---");
        Connection conn = DBHandler.connectDB();
        
        String query = "SELECT * FROM register_employee ORDER BY id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.println("Employee " + count + ": ID=" + rs.getString("id") + 
                             ", Name=" + rs.getString("name") + 
                             ", EmpID=" + rs.getString("emp_id"));
        }
        
        System.out.println("✓ Loaded " + count + " employees successfully");
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    private static void testDeleteOperation() throws Exception {
        System.out.println("\n--- Testing Delete Operation ---");
        Connection conn = DBHandler.connectDB();
        
        try {
            // Start transaction
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
            
            System.out.println("Deleting employee: " + employeeName + " (ID: " + employeeId + ")");
            
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
            
            // Commit transaction
            conn.commit();
            conn.setAutoCommit(true);
            
            System.out.println("✓ Employee deleted successfully");
            
            // Verify deletion
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM register_employee WHERE id = " + employeeId);
            if (rs.next() && rs.getInt("count") == 0) {
                System.out.println("✓ Employee removed from main table");
            } else {
                throw new Exception("Employee still exists in main table");
            }
            
            // Verify in deleted table
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM deleted_employees WHERE original_id = " + employeeId);
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("✓ Employee moved to deleted table");
            } else {
                throw new Exception("Employee not found in deleted table");
            }
            
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
    
    private static void testRestoreOperation() throws Exception {
        System.out.println("\n--- Testing Restore Operation ---");
        Connection conn = DBHandler.connectDB();
        
        try {
            // Start transaction
            conn.setAutoCommit(false);
            
            // Get first deleted employee
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM deleted_employees LIMIT 1");
            
            if (!rs.next()) {
                throw new Exception("No deleted employees found to restore");
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
            
            System.out.println("Restoring employee: " + employeeName + " (Original ID: " + originalId + ")");
            
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
            
            // Commit transaction
            conn.commit();
            conn.setAutoCommit(true);
            
            System.out.println("✓ Employee restored successfully");
            
            // Verify restoration
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM register_employee WHERE id = " + originalId);
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("✓ Employee restored to main table");
            } else {
                throw new Exception("Employee not found in main table after restoration");
            }
            
            // Verify removed from deleted table
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM deleted_employees WHERE id = " + deletedId);
            if (rs.next() && rs.getInt("count") == 0) {
                System.out.println("✓ Employee removed from deleted table");
            } else {
                throw new Exception("Employee still exists in deleted table");
            }
            
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

