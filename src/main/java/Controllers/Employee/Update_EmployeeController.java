/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Employee;

import Model.EmployeeDetails;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import DBConnection.DBHandler;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Update_EmployeeController implements Initializable {

    @FXML
    private TextField reg_txt_id;
    @FXML
    private TextField reg_txt_username;
    @FXML
    private TextField reg_txt_email;
    @FXML
    private TextField reg_txt_phnmb;
    @FXML
    private TextField reg_txt_nic;
    @FXML
    private TextField reg_txt_address;
    @FXML
    private TextField reg_txt_position;
    @FXML
    private Button btn_update_employee;
    @FXML
    private Button btn_refersh;

    // Initialize observable list to database
    private ObservableList<EmployeeDetails> data;

    @FXML
    private TableView<EmployeeDetails> tableEmployee;
    @FXML
    private TableColumn<EmployeeDetails, String> col_id;
    @FXML
    private TableColumn<EmployeeDetails, String> col_name;
    @FXML
    private TableColumn<EmployeeDetails, String> col_email;
    @FXML
    private TableColumn<EmployeeDetails, String> col_phonenumber;
    @FXML
    private TableColumn<EmployeeDetails, String> col_nic;
    @FXML
    private TableColumn<EmployeeDetails, String> col_address;
    @FXML
    private TableColumn<EmployeeDetails, String> col_position;

    @FXML
    private ScrollPane employeeTableScrollPane;

    /**
     * Initializes the controller class.
     */
    private Connection connection;
    private PreparedStatement pst;

    @FXML
    private Button btn_back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Update Employee Controller initialized");

        // Initialize table columns first
        initializeTableColumns();

        // make ID field non-editable (fixed)
        reg_txt_id.setEditable(false);

        // Then load employee data
        autoRefresh();

        System.out.println("Update Employee initialization completed");
    }

    private void initializeTableColumns() {
        // Set cell value factories
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("tel"));
        col_nic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_position.setCellValueFactory(new PropertyValueFactory<>("position"));

        // Set column resize policy
        autoSizeColumns();

        System.out.println("Table columns initialized");
    }

    private void autoRefresh() {
        System.out.println("Starting autoRefresh...");

        connection = DBHandler.connectDB();
        if (connection == null) {
            System.err.println("Database connection is null!");
            JOptionPane.showMessageDialog(null, "Database connection failed! Please check your database setup.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Database connected successfully");
        data = FXCollections.observableArrayList();

        try {
            // Execute query with specific columns to avoid timestamp confusion
            String query = "SELECT id, name, emp_id, email, tel, nic, address, department, position, emg_tel FROM register_employee";
            System.out.println("Executing query: " + query);

            ResultSet rs = connection.createStatement().executeQuery(query);

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                // get string from db using column names for clarity
                String emgTelValue = rs.getObject("emg_tel") != null ? rs.getString("emg_tel") : "";

                EmployeeDetails employee = new EmployeeDetails(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("emp_id"),
                        rs.getString("email"),
                        rs.getString("nic"),
                        rs.getString("tel"),
                        rs.getString("address"),
                        rs.getString("department"),
                        rs.getString("position"),
                        emgTelValue);

                data.add(employee);
                System.out.println("Added employee: " + rs.getString("name"));
            }

            System.out.println("Total employees loaded: " + rowCount);
            rs.close();

        } catch (SQLException ex) {
            System.err.println("SQL Error in autoRefresh: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error loading employee data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        // Set data to table
        System.out.println("Setting data to table...");
        tableEmployee.setItems(null);
        tableEmployee.setItems(data);

        System.out.println("Table data set. Items count: " + tableEmployee.getItems().size());

        // Refresh table view
        tableEmployee.refresh();

        System.out.println("autoRefresh completed successfully");
    }

    @FXML
    private void updateEmployeeButtonAction(MouseEvent event) {
        String id = reg_txt_id.getText();
        String userName = reg_txt_username.getText();
        String email = reg_txt_email.getText();
        String phoneNumber = reg_txt_phnmb.getText();
        String nic = reg_txt_nic.getText();
        String address = reg_txt_address.getText();
        String position = reg_txt_position.getText();

        if (id.equals("") || userName.equals("")
                || email.equals("")
                || phoneNumber.equals("")
                || nic.equals("")
                || address.equals("")
                || position.equals("")) {
            JOptionPane.showMessageDialog(null, "Some Fields are missing!");
        } else {
            String update = "UPDATE register_employee SET name = ?, email = ?, tel = ?, nic = ?, address = ?, position = ? WHERE id = ?";
            connection = DBHandler.connectDB();

            if (connection == null) {
                JOptionPane.showMessageDialog(null, "Database connection failed! Please check your database setup.",
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                pst = connection.prepareStatement(update);
                pst.setString(1, reg_txt_username.getText());
                pst.setString(2, reg_txt_email.getText());
                pst.setString(3, reg_txt_phnmb.getText());
                pst.setString(4, reg_txt_nic.getText());
                pst.setString(5, reg_txt_address.getText());
                pst.setString(6, reg_txt_position.getText());
                pst.setString(7, reg_txt_id.getText());

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Employee updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    autoRefresh();
                    // Clear form fields after successful update
                    clearFormFields();
                } else {
                    JOptionPane.showMessageDialog(null, "No employee found with the specified ID!",
                            "Update Failed", JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException ex) {
                Logger.getLogger(Update_EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error updating employee: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (pst != null)
                        pst.close();
                } catch (SQLException ex) {
                    System.err.println("Error closing PreparedStatement: " + ex.getMessage());
                }
            }
        }
    }

    private void clearFormFields() {
        reg_txt_id.clear();
        reg_txt_username.clear();
        reg_txt_email.clear();
        reg_txt_phnmb.clear();
        reg_txt_nic.clear();
        reg_txt_address.clear();
        reg_txt_position.clear();
    }

    private void autoSizeColumns() {
        // Set column resize policy for better scrolling experience
        tableEmployee.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Set minimum widths for better readability
        col_id.setMinWidth(60);
        col_name.setMinWidth(140);
        col_email.setMinWidth(160);
        col_phonenumber.setMinWidth(130);
        col_nic.setMinWidth(180);
        col_address.setMinWidth(160);
        col_position.setMinWidth(140);
    }

    @FXML
    private void refreshButtionClickAction(MouseEvent event) {
        System.out.println("Refresh button clicked");
        // Clear all text fields
        clearFormFields();
        // Refresh the table data
        autoRefresh();
    }

    @FXML
    private void displaySelectedAction(MouseEvent event) {
        EmployeeDetails employee = tableEmployee.getSelectionModel().getSelectedItem();
        if (employee == null) {
            JOptionPane.showMessageDialog(null, "Nothing Selected!");
        } else {
            String id = employee.getId();
            String name = employee.getName();
            String email = employee.getEmail();
            String phonenumber = employee.getTel();
            String nic = employee.getNic();
            String address = employee.getAddress();
            String position = employee.getPosition();

            reg_txt_id.setText(id);
            reg_txt_username.setText(name);
            reg_txt_email.setText(email);
            reg_txt_phnmb.setText(phonenumber);
            reg_txt_nic.setText(nic);
            reg_txt_address.setText(address);
            reg_txt_position.setText(position);
        }
    }

    // Back button functionality removed since Update Employee now loads in content
    // area

}