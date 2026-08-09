/*
 * Check the exact column names in the database
 */
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckColumns {
    
    public static void main(String[] args) {
        System.out.println("=== Checking Column Names ===");
        
        try {
            Connection conn = DBHandler.connectDB();
            Statement stmt = conn.createStatement();
            
            // Get column names
            ResultSet rs = stmt.executeQuery("SELECT * FROM register_employee LIMIT 1");
            var metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            System.out.println("Column names in register_employee:");
            for (int i = 1; i <= columnCount; i++) {
                System.out.println("  " + i + ": " + metaData.getColumnName(i) + " (" + metaData.getColumnTypeName(i) + ")");
            }
            
            // Test a simple query
            System.out.println("\nTesting simple query:");
            rs = stmt.executeQuery("SELECT id, name, emp_id FROM register_employee LIMIT 1");
            if (rs.next()) {
                System.out.println("ID: " + rs.getString("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Emp ID: " + rs.getString("emp_id"));
            }
            
            stmt.close();
            conn.close();
            
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

