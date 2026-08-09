/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class DBHandler {
    
    // Database configuration constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostel_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    
    private static Connection connection = null;

    public static Connection connectDB() {
        try {
            // Load the MySQL JDBC driver
            Class.forName(DRIVER_CLASS);
            
            // Create connection if it doesn't exist or is closed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            }
            
            return connection;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("MySQL JDBC Driver not found: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Database connection failed: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Unexpected error: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Close the database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully!");
            }
        } catch (SQLException ex) {
            System.err.println("Error closing database connection: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection testConn = connectDB();
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("Database connection test successful!");
                return true;
            }
        } catch (Exception ex) {
            System.err.println("Database connection test failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
    
}
