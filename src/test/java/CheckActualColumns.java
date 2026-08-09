/*
 * Check what columns actually exist in the database
 */
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckActualColumns {
    
    public static void main(String[] args) {
        System.out.println("=== Checking Actual Database Columns ===");
        
        try {
            Connection conn = DBHandler.connectDB();
            Statement stmt = conn.createStatement();
            
            // Get column names using DESCRIBE
            System.out.println("Table structure:");
            ResultSet rs = stmt.executeQuery("DESCRIBE register_employee");
            while (rs.next()) {
                System.out.println("Column: " + rs.getString(1) + " | Type: " + rs.getString(2));
            }
            
            // Test different column names
            System.out.println("\nTesting column access:");
            rs = stmt.executeQuery("SELECT * FROM register_employee LIMIT 1");
            if (rs.next()) {
                try {
                    System.out.println("id: " + rs.getString("id"));
                } catch (Exception e) {
                    System.out.println("id column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("name: " + rs.getString("name"));
                } catch (Exception e) {
                    System.out.println("name column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("emp_id: " + rs.getString("emp_id"));
                } catch (Exception e) {
                    System.out.println("emp_id column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("email: " + rs.getString("email"));
                } catch (Exception e) {
                    System.out.println("email column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("nic: " + rs.getString("nic"));
                } catch (Exception e) {
                    System.out.println("nic column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("tel: " + rs.getString("tel"));
                } catch (Exception e) {
                    System.out.println("tel column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("address: " + rs.getString("address"));
                } catch (Exception e) {
                    System.out.println("address column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("department: " + rs.getString("department"));
                } catch (Exception e) {
                    System.out.println("department column error: " + e.getMessage());
                }
                
                try {
                    System.out.println("position: " + rs.getString("position"));
                } catch (Exception e) {
                    System.out.println("position column error: " + e.getMessage());
                }
            }
            
            stmt.close();
            conn.close();
            
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

