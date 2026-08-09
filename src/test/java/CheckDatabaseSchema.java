/*
 * Check the actual database schema
 */
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckDatabaseSchema {
    
    public static void main(String[] args) {
        System.out.println("=== Checking Database Schema ===");
        
        try {
            Connection conn = DBHandler.connectDB();
            Statement stmt = conn.createStatement();
            
            // Check register_employee table structure
            System.out.println("\n--- register_employee table structure ---");
            ResultSet rs = stmt.executeQuery("DESCRIBE register_employee");
            while (rs.next()) {
                System.out.println("Column: " + rs.getString(1) + 
                                 " | Type: " + rs.getString(2) + 
                                 " | Null: " + rs.getString(3) + 
                                 " | Key: " + rs.getString(4));
            }
            
            // Check sample data
            System.out.println("\n--- Sample data from register_employee ---");
            rs = stmt.executeQuery("SELECT * FROM register_employee LIMIT 3");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString(1) + 
                                 " | Name: " + rs.getString(2) + 
                                 " | Email: " + rs.getString(4));
            }
            
            stmt.close();
            conn.close();
            
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
