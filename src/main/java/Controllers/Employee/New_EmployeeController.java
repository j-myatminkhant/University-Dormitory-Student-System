/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Employee;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import DBConnection.DBHandler;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * FXML Controller class
 *
 * @author User
 */
public class New_EmployeeController implements Initializable {

    @FXML
    private Button btn_back;
    @FXML
    private TextField reg_txt_emp_username;
    @FXML
    private TextField reg_txt_emp_phnmb;
    @FXML
    private TextField reg_txt_emp_nic;
    @FXML
    private Button btn_reg_employee;
    @FXML
    private TextField reg_txt_emp_emgtel;
    @FXML
    private TextField reg_txt_emp_email;
    @FXML
    private TextField reg_txt_emp_id;
    @FXML
    private TextField reg_txt_emp_department;
    @FXML
    private TextField reg_txt_emp_position;

    /**
     * Initializes the controller class.
     */
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        handler = new DBHandler();
        
        // Add validation listeners
        setupValidationListeners();
    }
    
    private void setupValidationListeners() {
        // Staff Name validation - only alphabetic characters
        reg_txt_emp_username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z\\s]*$")) {
                setTextFieldError(reg_txt_emp_username, "Only alphabet characters are allowed");
            } else {
                clearTextFieldError(reg_txt_emp_username);
            }
        });
        
        // NRC validation
        reg_txt_emp_nic.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidNRC(newValue)) {
                setTextFieldError(reg_txt_emp_nic, "Invalid NRC format. Example: 7/PMN(N)111111");
            } else {
                clearTextFieldError(reg_txt_emp_nic);
            }
        });
        
        // Contact number validation
        reg_txt_emp_phnmb.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^0\\d{8}(\\d{2})?$")) {
                setTextFieldError(reg_txt_emp_phnmb, "Contact number must be 9 or 11 digits starting with 0");
            } else {
                clearTextFieldError(reg_txt_emp_phnmb);
            }
        });
        
        // Email validation - must contain @gmail.com
        reg_txt_emp_email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.contains("@gmail.com")) {
                setTextFieldError(reg_txt_emp_email, "Email must contain @gmail.com");
            } else {
                clearTextFieldError(reg_txt_emp_email);
            }
        });
    }
    
    private boolean isValidNRC(String nrc) {
        String nrcRegex = "^(?:[1-9]|1[0-4])/[A-Z]{3}\\(N\\)\\d{6}$";
        Pattern pattern = Pattern.compile(nrcRegex);
        return pattern.matcher(nrc).matches();
    }
    
    private void setTextFieldError(TextField textField, String errorMessage) {
        textField.setStyle("-fx-background-radius: 10px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 10px;");
        textField.setPromptText(errorMessage);
    }
    
    private void clearTextFieldError(TextField textField) {
        textField.setStyle("-fx-background-radius: 10px;");
        // Restore original prompt text
        if (textField == reg_txt_emp_username) {
            textField.setPromptText("Staff Name");
        } else if (textField == reg_txt_emp_nic) {
            textField.setPromptText("NRC No (e.g., 7/PMN(N)111111)");
        } else if (textField == reg_txt_emp_phnmb) {
            textField.setPromptText("Contact No");
        } else if (textField == reg_txt_emp_emgtel) {
            textField.setPromptText("Address");
        } else if (textField == reg_txt_emp_email) {
            textField.setPromptText("abc@gmail.com");
        }
    }

    @FXML
    private void back_btn_clicked(MouseEvent event) throws IOException {
        btn_back.getScene().getWindow().hide();

        Stage stu_Menu = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Employee/Employee_Menu.fxml"));
        Scene scene = new Scene(root);
        stu_Menu.initStyle(StageStyle.TRANSPARENT);
        stu_Menu.setScene(scene);
        stu_Menu.show();
        stu_Menu.setResizable(false);
    }

    @FXML
    private void registerButtonAction(MouseEvent event) {
        String name = reg_txt_emp_username.getText();
        String empId = reg_txt_emp_id.getText();
        String email = reg_txt_emp_email.getText();
        String nic = reg_txt_emp_nic.getText();
        String tel = reg_txt_emp_phnmb.getText();
        String address = reg_txt_emp_emgtel.getText();
        String department = reg_txt_emp_department.getText();
        String position = reg_txt_emp_position.getText();

        // Validation checks
        if (name.equals("") || empId.equals("") || email.equals("") || nic.equals("") || tel.equals("") || address.equals("") || department.equals("") || position.equals("")) {
            JOptionPane.showMessageDialog(null, "All Fields Are Required!");
            return;
        }
        
        // Validate name (only alphabetic characters)
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(null, "Staff name must contain only alphabetic characters!");
            return;
        }
        
        // Validate NRC format
        if (!isValidNRC(nic)) {
            JOptionPane.showMessageDialog(null, "Invalid NRC format. Example: 7/PMN(N)111111");
            return;
        }
        
        // Validate contact number (9 or 11 digits starting with 0)
        if (!tel.matches("^0\\d{8}(\\d{2})?$")) {
            JOptionPane.showMessageDialog(null, "Contact number must be 9 or 11 digits starting with 0!");
            return;
        }
        
        // Validate email format - must contain @gmail.com
        if (!email.contains("@gmail.com")) {
            JOptionPane.showMessageDialog(null, "Email must contain @gmail.com!");
            return;
        }

        // All validations passed, proceed with registration
        String insert = "INSERT INTO register_employee(name,emp_id,email,nic,tel,address,department,position)" + "VALUES(?,?,?,?,?,?,?,?)";
        connection = handler.connectDB();
        try {
            pst = connection.prepareStatement(insert);
            pst.setString(1, reg_txt_emp_username.getText());
            pst.setString(2, reg_txt_emp_id.getText());
            pst.setString(3, reg_txt_emp_email.getText());
            pst.setString(4, reg_txt_emp_nic.getText());
            pst.setString(5, reg_txt_emp_phnmb.getText());
            pst.setString(6, reg_txt_emp_emgtel.getText());
            pst.setString(7, reg_txt_emp_department.getText());
            pst.setString(8, reg_txt_emp_position.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Employee Registered Successfully!");
            
            // Clear form fields
            setTExtRefresh();
            
            // Navigate to View Employee page automatically
            navigateToViewEmployees();
            
        } catch (SQLException ex) {
            Logger.getLogger(New_EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void redirectToLiveInStaff() throws IOException {
        btn_reg_employee.getScene().getWindow().hide();

        Stage liveInStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Employee/All_Employee_Living.fxml"));
        Scene scene = new Scene(root);
        liveInStage.initStyle(StageStyle.TRANSPARENT);
        liveInStage.setScene(scene);
        liveInStage.show();
        liveInStage.setResizable(false);
        
        // Add mouse drag functionality
        root.setOnMousePressed(event -> {
            double xoffset = event.getSceneX();
            double yoffset = event.getSceneY();
            root.setOnMouseDragged(dragEvent -> {
                liveInStage.setX(dragEvent.getScreenX() - xoffset);
                liveInStage.setY(dragEvent.getScreenY() - yoffset);
            });
        });
    }

    private void setTExtRefresh() {
        reg_txt_emp_username.setText("");
        reg_txt_emp_id.setText("");
        reg_txt_emp_email.setText("");
        reg_txt_emp_nic.setText("");
        reg_txt_emp_phnmb.setText("");
        reg_txt_emp_emgtel.setText("");
        reg_txt_emp_department.setText("");
        reg_txt_emp_position.setText("");
        
        // Clear all validation error styles
        clearTextFieldError(reg_txt_emp_username);
        clearTextFieldError(reg_txt_emp_id);
        clearTextFieldError(reg_txt_emp_email);
        clearTextFieldError(reg_txt_emp_nic);
        clearTextFieldError(reg_txt_emp_phnmb);
        clearTextFieldError(reg_txt_emp_emgtel);
        clearTextFieldError(reg_txt_emp_department);
        clearTextFieldError(reg_txt_emp_position);
    }
    
    /**
     * Navigate to View Employees page after successful registration
     * This matches the Student registration pattern
     */
    private void navigateToViewEmployees() {
        try {
            // Close current registration window (if opened as separate window)
            // btn_reg_employee.getScene().getWindow().hide();
            
            // Open Employee Management window with view employees page loaded
            Stage employeeManageStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Employee/Employee_Menu.fxml"));
            Parent root = loader.load();
            
            // Get the controller and load the view employees page
            Employee_MenuController controller = loader.getController();
            controller.loadViewEmployeesPage();
            
            Scene scene = new Scene(root);
            employeeManageStage.setScene(scene);
            employeeManageStage.initStyle(StageStyle.TRANSPARENT);
            employeeManageStage.show();
            employeeManageStage.setResizable(false);
            
        } catch (IOException e) {
            System.err.println("Error navigating to view employees: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
