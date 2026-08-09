/*
 * Fix the employee table structure to match application requirements
 */
import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.Statement;

public class FixEmployeeTable {
    
    public static void main(String[] args) {
        System.out.println("=== Fixing Employee Table Structure ===");
        
        try {
            Connection conn = DBHandler.connectDB();
            Statement stmt = conn.createStatement();
            
            // Add missing columns
            System.out.println("Adding missing columns...");
            
            try {
                stmt.executeUpdate("ALTER TABLE register_employee ADD COLUMN emp_id VARCHAR(50) UNIQUE AFTER name");
                System.out.println("✓ Added emp_id column");
            } catch (Exception ex) {
                System.out.println("⚠ emp_id column already exists or error: " + ex.getMessage());
            }
            
            try {
                stmt.executeUpdate("ALTER TABLE register_employee ADD COLUMN email VARCHAR(100) AFTER emp_id");
                System.out.println("✓ Added email column");
            } catch (Exception ex) {
                System.out.println("⚠ email column already exists or error: " + ex.getMessage());
            }
            
            try {
                stmt.executeUpdate("ALTER TABLE register_employee ADD COLUMN address TEXT AFTER tel");
                System.out.println("✓ Added address column");
            } catch (Exception ex) {
                System.out.println("⚠ address column already exists or error: " + ex.getMessage());
            }
            
            try {
                stmt.executeUpdate("ALTER TABLE register_employee ADD COLUMN department VARCHAR(100) AFTER address");
                System.out.println("✓ Added department column");
            } catch (Exception ex) {
                System.out.println("⚠ department column already exists or error: " + ex.getMessage());
            }
            
            try {
                stmt.executeUpdate("ALTER TABLE register_employee ADD COLUMN position VARCHAR(100) AFTER department");
                System.out.println("✓ Added position column");
            } catch (Exception ex) {
                System.out.println("⚠ position column already exists or error: " + ex.getMessage());
            }
            
            // Update existing records with sample data
            System.out.println("Updating existing records...");
            stmt.executeUpdate("UPDATE register_employee SET emp_id = CONCAT('EMP', LPAD(id, 3, '0')) WHERE emp_id IS NULL OR emp_id = ''");
            stmt.executeUpdate("UPDATE register_employee SET email = CONCAT('employee', id, '@gmail.com') WHERE email IS NULL OR email = ''");
            stmt.executeUpdate("UPDATE register_employee SET address = CONCAT('Address for Employee ', name) WHERE address IS NULL OR address = ''");
            stmt.executeUpdate("UPDATE register_employee SET department = 'General' WHERE department IS NULL OR department = ''");
            stmt.executeUpdate("UPDATE register_employee SET position = 'Staff' WHERE position IS NULL OR position = ''");
            System.out.println("✓ Updated existing records");
            
            // Create deleted_employees table
            System.out.println("Creating deleted_employees table...");
            String createDeletedTable = "CREATE TABLE IF NOT EXISTS deleted_employees (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "original_id INT NOT NULL, " +
                "name VARCHAR(100) NOT NULL, " +
                "emp_id VARCHAR(50) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "nic VARCHAR(20) NOT NULL, " +
                "tel VARCHAR(20) NOT NULL, " +
                "address TEXT NOT NULL, " +
                "department VARCHAR(100) NOT NULL, " +
                "position VARCHAR(100) NOT NULL, " +
                "deleted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "deleted_by VARCHAR(100) DEFAULT 'SYSTEM'" +
                ")";
            stmt.executeUpdate(createDeletedTable);
            System.out.println("✓ Created deleted_employees table");
            
            // Show final table structure
            System.out.println("\n--- Final register_employee table structure ---");
            var rs = stmt.executeQuery("DESCRIBE register_employee");
            while (rs.next()) {
                System.out.println("Column: " + rs.getString(1) + 
                                 " | Type: " + rs.getString(2) + 
                                 " | Null: " + rs.getString(3) + 
                                 " | Key: " + rs.getString(4));
            }
            
            // Show sample data
            System.out.println("\n--- Sample data from register_employee ---");
            rs = stmt.executeQuery("SELECT id, name, emp_id, email, department, position FROM register_employee LIMIT 3");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString(1) + 
                                 " | Name: " + rs.getString(2) + 
                                 " | Emp ID: " + rs.getString(3) + 
                                 " | Email: " + rs.getString(4) + 
                                 " | Dept: " + rs.getString(5) + 
                                 " | Position: " + rs.getString(6));
            }
            
            stmt.close();
            conn.close();
            
            System.out.println("\n✓ Employee table structure fixed successfully!");
            
        } catch (Exception ex) {
            System.err.println("Error fixing employee table: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

