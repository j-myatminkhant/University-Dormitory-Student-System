/*
 * Test class for Delete Employee functionality
 * Tests the complete delete and restore workflow
 */
import DBConnection.DBHandler;
import Model.EmployeeDetails;
import Model.DeletedEmployeeDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteEmployeeTest {
    
    public static void main(String[] args) {
        System.out.println("=== Delete Employee Functionality Test ===");
        
        try {
            // Test 1: Database Connection
            testDatabaseConnection();
            
            // Test 2: Create test employee
            int testEmployeeId = createTestEmployee();
            System.out.println("✓ Created test employee with ID: " + testEmployeeId);
            
            // Test 3: Verify employee exists in main table
            verifyEmployeeExists(testEmployeeId);
            
            // Test 4: Test delete functionality
            testDeleteEmployee(testEmployeeId);
            
            // Test 5: Verify employee moved to deleted table
            verifyEmployeeInDeletedTable(testEmployeeId);
            
            // Test 6: Test restore functionality
            testRestoreEmployee(testEmployeeId);
            
            // Test 7: Verify employee restored to main table
            verifyEmployeeRestored(testEmployeeId);
            
            // Test 8: Clean up test data
            cleanupTestData(testEmployeeId);
            
            System.out.println("\n✓ All tests passed successfully!");
            
        } catch (Exception ex) {
            System.err.println("✗ Test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private static void testDatabaseConnection() throws SQLException {
        System.out.println("\n--- Testing Database Connection ---");
        Connection conn = DBHandler.connectDB();
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Database connection failed");
        }
        System.out.println("✓ Database connection successful");
        conn.close();
    }
    
    private static int createTestEmployee() throws SQLException {
        System.out.println("\n--- Creating Test Employee ---");
        Connection conn = DBHandler.connectDB();
        
        // Insert test employee
        String insertQuery = "INSERT INTO register_employee (name, emp_id, email, nic, tel, address, department, position, emg_tel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Test Employee");
        pst.setString(2, "TEST001");
        pst.setString(3, "test@example.com");
        pst.setString(4, "TEST123456789");
        pst.setString(5, "+94-71-999-9999");
        pst.setString(6, "Test Address");
        pst.setString(7, "Test Department");
        pst.setString(8, "Test Position");
        pst.setString(9, "+94-71-999-9998");
        
        int rowsAffected = pst.executeUpdate();
        if (rowsAffected != 1) {
            throw new SQLException("Failed to create test employee");
        }
        
        // Get generated ID
        ResultSet rs = pst.getGeneratedKeys();
        int employeeId = -1;
        if (rs.next()) {
            employeeId = rs.getInt(1);
        }
        
        rs.close();
        pst.close();
        conn.close();
        
        return employeeId;
    }
    
    private static void verifyEmployeeExists(int employeeId) throws SQLException {
        System.out.println("\n--- Verifying Employee Exists in Main Table ---");
        Connection conn = DBHandler.connectDB();
        
        String query = "SELECT * FROM register_employee WHERE id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, employeeId);
        
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            throw new SQLException("Employee not found in main table");
        }
        
        System.out.println("✓ Employee found in main table: " + rs.getString("name"));
        
        rs.close();
        pst.close();
        conn.close();
    }
    
    private static void testDeleteEmployee(int employeeId) throws SQLException {
        System.out.println("\n--- Testing Delete Employee ---");
        Connection conn = DBHandler.connectDB();
        
        try {
            // Start transaction
            conn.setAutoCommit(false);
            
            // Get employee data before deletion
            String selectQuery = "SELECT * FROM register_employee WHERE id = ?";
            PreparedStatement selectPst = conn.prepareStatement(selectQuery);
            selectPst.setInt(1, employeeId);
            ResultSet rs = selectPst.executeQuery();
            
            if (!rs.next()) {
                throw new SQLException("Employee not found for deletion");
            }
            
            // Insert into deleted_employees table
            String insertDeleted = "INSERT INTO deleted_employees (original_id, name, emp_id, email, nic, tel, address, department, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPst = conn.prepareStatement(insertDeleted);
            insertPst.setString(1, String.valueOf(employeeId));
            insertPst.setString(2, rs.getString("name"));
            insertPst.setString(3, rs.getString("emp_id"));
            insertPst.setString(4, rs.getString("email"));
            insertPst.setString(5, rs.getString("nic"));
            insertPst.setString(6, rs.getString("tel"));
            insertPst.setString(7, rs.getString("address"));
            insertPst.setString(8, rs.getString("department"));
            insertPst.setString(9, rs.getString("position"));
            insertPst.executeUpdate();
            
            // Delete from register_employee table
            String deleteQuery = "DELETE FROM register_employee WHERE id = ?";
            PreparedStatement deletePst = conn.prepareStatement(deleteQuery);
            deletePst.setInt(1, employeeId);
            int deletedRows = deletePst.executeUpdate();
            
            if (deletedRows != 1) {
                throw new SQLException("Failed to delete employee from main table");
            }
            
            // Commit transaction
            conn.commit();
            conn.setAutoCommit(true);
            
            System.out.println("✓ Employee deleted successfully");
            
            rs.close();
            selectPst.close();
            insertPst.close();
            deletePst.close();
            
        } catch (SQLException ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw ex;
        } finally {
            conn.close();
        }
    }
    
    private static void verifyEmployeeInDeletedTable(int originalId) throws SQLException {
        System.out.println("\n--- Verifying Employee in Deleted Table ---");
        Connection conn = DBHandler.connectDB();
        
        String query = "SELECT * FROM deleted_employees WHERE original_id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, String.valueOf(originalId));
        
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            throw new SQLException("Employee not found in deleted table");
        }
        
        System.out.println("✓ Employee found in deleted table: " + rs.getString("name"));
        System.out.println("✓ Deleted date: " + rs.getString("deleted_date"));
        
        rs.close();
        pst.close();
        conn.close();
    }
    
    private static void testRestoreEmployee(int originalId) throws SQLException {
        System.out.println("\n--- Testing Restore Employee ---");
        Connection conn = DBHandler.connectDB();
        
        try {
            // Start transaction
            conn.setAutoCommit(false);
            
            // Get deleted employee data
            String selectQuery = "SELECT * FROM deleted_employees WHERE original_id = ?";
            PreparedStatement selectPst = conn.prepareStatement(selectQuery);
            selectPst.setString(1, String.valueOf(originalId));
            ResultSet rs = selectPst.executeQuery();
            
            if (!rs.next()) {
                throw new SQLException("Employee not found in deleted table for restoration");
            }
            
            // Insert back into register_employee table
            String insertEmployee = "INSERT INTO register_employee (id, name, emp_id, email, nic, tel, address, department, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPst = conn.prepareStatement(insertEmployee);
            insertPst.setString(1, rs.getString("original_id"));
            insertPst.setString(2, rs.getString("name"));
            insertPst.setString(3, rs.getString("emp_id"));
            insertPst.setString(4, rs.getString("email"));
            insertPst.setString(5, rs.getString("nic"));
            insertPst.setString(6, rs.getString("tel"));
            insertPst.setString(7, rs.getString("address"));
            insertPst.setString(8, rs.getString("department"));
            insertPst.setString(9, rs.getString("position"));
            insertPst.executeUpdate();
            
            // Remove from deleted_employees table
            String removeDeleted = "DELETE FROM deleted_employees WHERE original_id = ?";
            PreparedStatement removePst = conn.prepareStatement(removeDeleted);
            removePst.setString(1, String.valueOf(originalId));
            removePst.executeUpdate();
            
            // Commit transaction
            conn.commit();
            conn.setAutoCommit(true);
            
            System.out.println("✓ Employee restored successfully");
            
            rs.close();
            selectPst.close();
            insertPst.close();
            removePst.close();
            
        } catch (SQLException ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw ex;
        } finally {
            conn.close();
        }
    }
    
    private static void verifyEmployeeRestored(int employeeId) throws SQLException {
        System.out.println("\n--- Verifying Employee Restored to Main Table ---");
        Connection conn = DBHandler.connectDB();
        
        String query = "SELECT * FROM register_employee WHERE id = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, employeeId);
        
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            throw new SQLException("Employee not found in main table after restoration");
        }
        
        System.out.println("✓ Employee restored to main table: " + rs.getString("name"));
        
        rs.close();
        pst.close();
        conn.close();
    }
    
    private static void cleanupTestData(int employeeId) throws SQLException {
        System.out.println("\n--- Cleaning Up Test Data ---");
        Connection conn = DBHandler.connectDB();
        
        // Delete from main table
        String deleteMain = "DELETE FROM register_employee WHERE id = ?";
        PreparedStatement pst1 = conn.prepareStatement(deleteMain);
        pst1.setInt(1, employeeId);
        pst1.executeUpdate();
        pst1.close();
        
        // Delete from deleted table (if exists)
        String deleteDeleted = "DELETE FROM deleted_employees WHERE original_id = ?";
        PreparedStatement pst2 = conn.prepareStatement(deleteDeleted);
        pst2.setString(1, String.valueOf(employeeId));
        pst2.executeUpdate();
        pst2.close();
        
        conn.close();
        System.out.println("✓ Test data cleaned up");
    }
}
