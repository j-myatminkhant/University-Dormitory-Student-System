/*
 * Database Test Class for Hostel Management System
 * Used to verify database connectivity and basic operations
 */
package DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== Hostel Management System Database Test ===");
        
        // Test database connection
        if (testConnection()) {
            System.out.println("✓ Database connection successful!");
            
            // Test basic operations
            testBasicOperations();
            
            // Test table structures
            testTableStructures();
            
            // Test sample data
            testSampleData();
            
        } else {
            System.out.println("✗ Database connection failed!");
        }
    }
    
    /**
     * Test basic database connection
     */
    private static boolean testConnection() {
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database URL: " + conn.getMetaData().getURL());
                System.out.println("Database Product: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database Version: " + conn.getMetaData().getDatabaseProductVersion());
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("Connection test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * Test basic database operations
     */
    private static void testBasicOperations() {
        System.out.println("\n--- Testing Basic Operations ---");
        
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                // Test simple query
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1 as test");
                
                if (rs.next()) {
                    System.out.println("✓ Basic query execution successful");
                }
                
                rs.close();
                stmt.close();
                
            }
        } catch (SQLException ex) {
            System.err.println("Basic operations test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Test table structures
     */
    private static void testTableStructures() {
        System.out.println("\n--- Testing Table Structures ---");
        
        String[] tables = {
            "register_students",
            "register_employee", 
            "leaved_students",
            "leaved_employees",
            "student_fee",
            "employee_fee"
        };
        
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                
                for (String table : tables) {
                    try {
                        ResultSet rs = stmt.executeQuery("DESCRIBE " + table);
                        int columnCount = 0;
                        while (rs.next()) {
                            columnCount++;
                        }
                        rs.close();
                        
                        if (columnCount > 0) {
                            System.out.println("✓ Table '" + table + "' exists with " + columnCount + " columns");
                        } else {
                            System.out.println("✗ Table '" + table + "' exists but has no columns");
                        }
                        
                    } catch (SQLException ex) {
                        System.out.println("✗ Table '" + table + "' does not exist: " + ex.getMessage());
                    }
                }
                
                stmt.close();
            }
        } catch (SQLException ex) {
            System.err.println("Table structure test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Test sample data
     */
    private static void testSampleData() {
        System.out.println("\n--- Testing Sample Data ---");
        
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                
                // Test students count
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM register_students");
                if (rs.next()) {
                    int studentCount = rs.getInt("count");
                    System.out.println("✓ Students table has " + studentCount + " records");
                }
                rs.close();
                
                // Test employees count
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM register_employee");
                if (rs.next()) {
                    int employeeCount = rs.getInt("count");
                    System.out.println("✓ Employees table has " + employeeCount + " records");
                }
                rs.close();
                
                // Test fee records count
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM student_fee");
                if (rs.next()) {
                    int feeCount = rs.getInt("count");
                    System.out.println("✓ Student fee table has " + feeCount + " records");
                }
                rs.close();
                
                stmt.close();
            }
        } catch (SQLException ex) {
            System.err.println("Sample data test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Test database utility methods
     */
    public static void testUtilityMethods() {
        System.out.println("\n--- Testing Utility Methods ---");
        
        // Test record existence
        boolean studentExists = DatabaseUtility.recordExists("register_students", "nsbmID", "NSBM001");
        System.out.println("✓ Student NSBM001 exists: " + studentExists);
        
        // Test next ID
        int nextStudentId = DatabaseUtility.getNextId("register_students", "id");
        System.out.println("✓ Next student ID: " + nextStudentId);
        
        // Test get all records
        java.util.List<Object[]> students = DatabaseUtility.getAllRecords("register_students");
        System.out.println("✓ Retrieved " + students.size() + " student records");
    }
}
