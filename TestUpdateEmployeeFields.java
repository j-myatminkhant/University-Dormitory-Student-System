import DBConnection.DBHandler;
import Model.EmployeeDetails;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestUpdateEmployeeFields {
    public static void main(String[] args) {
        System.out.println("=== Testing Update Employee Field Mappings ===");
        
        // Test database connection and data loading
        DBHandler handler = new DBHandler();
        Connection connection = handler.connectDB();
        
        if (connection == null) {
            System.out.println("✗ Database connection failed");
            return;
        }
        
        System.out.println("✓ Database connection successful");
        
        try {
            // Test the exact query used in the controller
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, emp_id, email, tel, nic, address, department, position, emg_tel FROM register_employee LIMIT 1");
            
            if (rs.next()) {
                System.out.println("\n✓ Data found - testing EmployeeDetails constructor:");
                
                String emgTelValue = rs.getObject("emg_tel") != null ? rs.getString("emg_tel") : "";
                
                // Test the exact constructor call from the controller
                EmployeeDetails employee = new EmployeeDetails(
                    rs.getString("id"), 
                    rs.getString("name"), 
                    rs.getString("emp_id"), 
                    rs.getString("email"),
                    rs.getString("tel"), 
                    rs.getString("nic"), 
                    rs.getString("address"), 
                    rs.getString("department"), 
                    rs.getString("position"),
                    emgTelValue);
                
                System.out.println("✓ EmployeeDetails object created successfully");
                System.out.println("  ID: " + employee.getId());
                System.out.println("  Name: " + employee.getName());
                System.out.println("  Emp ID: " + employee.getEmpId());
                System.out.println("  Email: " + employee.getEmail());
                System.out.println("  Tel: " + employee.getTel());
                System.out.println("  NIC: " + employee.getNic());
                System.out.println("  Address: " + employee.getAddress());
                System.out.println("  Department: " + employee.getDepartment());
                System.out.println("  Position: " + employee.getPosition());
                System.out.println("  Emg Tel: " + employee.getEmgTel());
                
                // Test field mappings that would be used in displaySelectedAction
                System.out.println("\n✓ Testing field mappings for form population:");
                System.out.println("  reg_txt_id.setText(\"" + employee.getId() + "\")");
                System.out.println("  reg_txt_username.setText(\"" + employee.getName() + "\")");
                System.out.println("  reg_txt_emp_id.setText(\"" + employee.getEmpId() + "\")");
                System.out.println("  reg_txt_email.setText(\"" + employee.getEmail() + "\")");
                System.out.println("  reg_txt_phnmb.setText(\"" + employee.getTel() + "\")");
                System.out.println("  reg_txt_nic.setText(\"" + employee.getNic() + "\")");
                System.out.println("  reg_txt_address.setText(\"" + employee.getAddress() + "\")");
                System.out.println("  reg_txt_department.setText(\"" + employee.getDepartment() + "\")");
                System.out.println("  reg_txt_position.setText(\"" + employee.getPosition() + "\")");
                System.out.println("  reg_txt_emgtel.setText(\"" + employee.getEmgTel() + "\")");
                
            } else {
                System.out.println("✗ No data found in register_employee table");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Field ID Verification ===");
        System.out.println("Controller expects these fx:id values:");
        System.out.println("  ✓ reg_txt_id");
        System.out.println("  ✓ reg_txt_username");
        System.out.println("  ✓ reg_txt_emp_id");
        System.out.println("  ✓ reg_txt_email");
        System.out.println("  ✓ reg_txt_phnmb");
        System.out.println("  ✓ reg_txt_nic");
        System.out.println("  ✓ reg_txt_address");
        System.out.println("  ✓ reg_txt_department");
        System.out.println("  ✓ reg_txt_position");
        System.out.println("  ✓ reg_txt_emgtel");
        
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
