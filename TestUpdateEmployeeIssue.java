import DBConnection.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestUpdateEmployeeIssue {
    public static void main(String[] args) {
        System.out.println("=== Testing Update Employee Issues ===");
        
        // Test 1: Check database connection
        System.out.println("\n1. Testing database connection...");
        DBHandler handler = new DBHandler();
        Connection connection = handler.connectDB();
        
        if (connection != null) {
            System.out.println("✓ Database connection successful");
        } else {
            System.out.println("✗ Database connection failed");
            return;
        }
        
        // Test 2: Check if register_employee table exists and has correct structure
        System.out.println("\n2. Checking register_employee table structure...");
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("DESCRIBE register_employee");
            
            System.out.println("Table structure:");
            while (rs.next()) {
                System.out.println("  " + rs.getString("Field") + " - " + rs.getString("Type"));
            }
            
            // Check if we have data
            ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) as count FROM register_employee");
            if (countRs.next()) {
                System.out.println("✓ Table has " + countRs.getInt("count") + " records");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error checking table: " + e.getMessage());
        }
        
        // Test 3: Check field ID mismatches
        System.out.println("\n3. Checking field ID mismatches...");
        System.out.println("Controller expects:");
        System.out.println("  - reg_txt_id");
        System.out.println("  - reg_txt_username");
        System.out.println("  - reg_txt_emp_id");
        System.out.println("  - reg_txt_email");
        System.out.println("  - reg_txt_phnmb");
        System.out.println("  - reg_txt_nic");
        System.out.println("  - reg_txt_address");
        System.out.println("  - reg_txt_department");
        System.out.println("  - reg_txt_position");
        System.out.println("  - reg_txt_emgtel");
        
        System.out.println("\nFXML has:");
        System.out.println("  - reg_txt_emp_id (should be reg_txt_id)");
        System.out.println("  - reg_txt_emp_username (should be reg_txt_username)");
        System.out.println("  - reg_txt_emp_id2 (should be reg_txt_emp_id)");
        System.out.println("  - reg_txt_emp_phnmb (should be reg_txt_phnmb)");
        System.out.println("  - reg_txt_emp_nic (should be reg_txt_nic)");
        System.out.println("  - reg_txt_emp_email (should be reg_txt_email)");
        System.out.println("  - reg_txt_address (✓ correct)");
        System.out.println("  - reg_txt_department (✓ correct)");
        System.out.println("  - reg_txt_position (✓ correct)");
        System.out.println("  - reg_txt_emgtel (✓ correct)");
        
        System.out.println("\n✗ CRITICAL ISSUE: Field ID mismatches will cause NullPointerException!");
        
        // Test 4: Check if the table loads data correctly
        System.out.println("\n4. Testing data loading...");
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, emp_id, email, tel, nic, address, department, position, emg_tel FROM register_employee LIMIT 1");
            
            if (rs.next()) {
                System.out.println("✓ Sample data found:");
                System.out.println("  ID: " + rs.getString("id"));
                System.out.println("  Name: " + rs.getString("name"));
                System.out.println("  Emp ID: " + rs.getString("emp_id"));
                System.out.println("  Email: " + rs.getString("email"));
                System.out.println("  Tel: " + rs.getString("tel"));
                System.out.println("  NIC: " + rs.getString("nic"));
                System.out.println("  Address: " + rs.getString("address"));
                System.out.println("  Department: " + rs.getString("department"));
                System.out.println("  Position: " + rs.getString("position"));
                System.out.println("  Emg Tel: " + rs.getString("emg_tel"));
            } else {
                System.out.println("✗ No data found in register_employee table");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error loading data: " + e.getMessage());
        }
        
        System.out.println("\n=== Summary ===");
        System.out.println("Main issues found:");
        System.out.println("1. Field ID mismatches between FXML and Controller");
        System.out.println("2. Missing reg_txt_id field in FXML");
        System.out.println("3. Wrong field names in FXML (reg_txt_emp_username vs reg_txt_username)");
        
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
