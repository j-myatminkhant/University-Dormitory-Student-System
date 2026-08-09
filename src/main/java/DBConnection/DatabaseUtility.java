/*
 * Database Utility Class for Hostel Management System
 * Provides common database operations and helper methods
 */
package DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class DatabaseUtility {
    
    /**
     * Execute a SELECT query and return ResultSet
     * @param query SQL SELECT query
     * @return ResultSet containing the query results
     */
    public static ResultSet executeQuery(String query) {
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                return stmt.executeQuery(query);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Query execution failed: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Query execution failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Execute an INSERT, UPDATE, or DELETE query
     * @param query SQL query to execute
     * @param params Parameters for the prepared statement
     * @return Number of rows affected
     */
    public static int executeUpdate(String query, Object... params) {
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                PreparedStatement pst = conn.prepareStatement(query);
                
                // Set parameters
                for (int i = 0; i < params.length; i++) {
                    pst.setObject(i + 1, params[i]);
                }
                
                int result = pst.executeUpdate();
                pst.close();
                return result;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Update execution failed: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Update execution failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Check if a record exists in a table
     * @param tableName Name of the table
     * @param columnName Name of the column to check
     * @param value Value to check for
     * @return true if record exists, false otherwise
     */
    public static boolean recordExists(String tableName, String columnName, String value) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, value);
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    int count = rs.getInt(1);
                    rs.close();
                    pst.close();
                    return count > 0;
                }
                rs.close();
                pst.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error checking record existence: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get the next available ID for a table
     * @param tableName Name of the table
     * @param idColumn Name of the ID column
     * @return Next available ID
     */
    public static int getNextId(String tableName, String idColumn) {
        String query = "SELECT MAX(" + idColumn + ") FROM " + tableName;
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                if (rs.next()) {
                    int maxId = rs.getInt(1);
                    rs.close();
                    stmt.close();
                    return maxId + 1;
                }
                rs.close();
                stmt.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error getting next ID: " + ex.getMessage());
            ex.printStackTrace();
        }
        return 1; // Default to 1 if no records exist
    }
    
    /**
     * Get all records from a table
     * @param tableName Name of the table
     * @return List of all records as Object arrays
     */
    public static List<Object[]> getAllRecords(String tableName) {
        List<Object[]> records = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                int columnCount = rs.getMetaData().getColumnCount();
                
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    records.add(row);
                }
                
                rs.close();
                stmt.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all records: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return records;
    }
    
    /**
     * Delete a record from a table
     * @param tableName Name of the table
     * @param idColumn Name of the ID column
     * @param id Value of the ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteRecord(String tableName, String idColumn, int id) {
        String query = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
        int result = executeUpdate(query, id);
        return result > 0;
    }
    
    /**
     * Move a student to leaved_students table
     * @param studentId ID of the student to move
     * @param leaveDate Date when the student left
     * @return true if operation was successful, false otherwise
     */
    public static boolean moveStudentToLeaved(int studentId, String leaveDate) {
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                // First, get student details
                String selectQuery = "SELECT * FROM register_students WHERE id = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setInt(1, studentId);
                ResultSet rs = selectStmt.executeQuery();
                
                if (rs.next()) {
                    // Insert into leaved_students
                    String insertQuery = "INSERT INTO leaved_students (name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setString(1, rs.getString("name"));
                    insertStmt.setString(2, rs.getString("nsbmID"));
                    insertStmt.setString(3, rs.getString("email"));
                    insertStmt.setString(4, rs.getString("phoneNumber"));
                    insertStmt.setString(5, rs.getString("nic"));
                    insertStmt.setString(6, rs.getString("address"));
                    insertStmt.setString(7, rs.getString("guardName"));
                    insertStmt.setString(8, rs.getString("guardTel"));
                    insertStmt.setString(9, leaveDate);
                    
                    int insertResult = insertStmt.executeUpdate();
                    
                    if (insertResult > 0) {
                        // Delete from register_students
                        String deleteQuery = "DELETE FROM register_students WHERE id = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                        deleteStmt.setInt(1, studentId);
                        int deleteResult = deleteStmt.executeUpdate();
                        
                        rs.close();
                        selectStmt.close();
                        insertStmt.close();
                        deleteStmt.close();
                        
                        return deleteResult > 0;
                    }
                    
                    rs.close();
                    selectStmt.close();
                    insertStmt.close();
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error moving student to leaved: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
    
    /**
     * Move an employee to leaved_employees table
     * @param employeeId ID of the employee to move
     * @param leaveDate Date when the employee left
     * @return true if operation was successful, false otherwise
     */
    public static boolean moveEmployeeToLeaved(int employeeId, String leaveDate) {
        try {
            Connection conn = DBHandler.connectDB();
            if (conn != null) {
                // First, get employee details
                String selectQuery = "SELECT * FROM register_employee WHERE id = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setInt(1, employeeId);
                ResultSet rs = selectStmt.executeQuery();
                
                if (rs.next()) {
                    // Insert into leaved_employees
                    String insertQuery = "INSERT INTO leaved_employees (name, nic, tel, emg_tel, date) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setString(1, rs.getString("name"));
                    insertStmt.setString(2, rs.getString("nic"));
                    insertStmt.setString(3, rs.getString("tel"));
                    insertStmt.setString(4, rs.getString("emg_tel"));
                    insertStmt.setString(5, leaveDate);
                    
                    int insertResult = insertStmt.executeUpdate();
                    
                    if (insertResult > 0) {
                        // Delete from register_employee
                        String deleteQuery = "DELETE FROM register_employee WHERE id = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                        deleteStmt.setInt(1, employeeId);
                        int deleteResult = deleteStmt.executeUpdate();
                        
                        rs.close();
                        selectStmt.close();
                        insertStmt.close();
                        deleteStmt.close();
                        
                        return deleteResult > 0;
                    }
                    
                    rs.close();
                    selectStmt.close();
                    insertStmt.close();
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error moving employee to leaved: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
}
