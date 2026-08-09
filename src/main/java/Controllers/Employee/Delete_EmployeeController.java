/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Employee;

import DBConnection.DBHandler;
import Model.EmployeeDetails;
import Model.DeletedEmployeeDetails;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Delete_EmployeeController implements Initializable {

    // Main employee table
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
    
    // Deleted employees table
    @FXML
    private TableView<DeletedEmployeeDetails> tableDeletedEmployees;
    @FXML
    private TableColumn<DeletedEmployeeDetails, String> col_deleted_id;
    @FXML
    private TableColumn<DeletedEmployeeDetails, String> col_deleted_name;
    @FXML
    private TableColumn<DeletedEmployeeDetails, String> col_deleted_email;
    @FXML
    private TableColumn<DeletedEmployeeDetails, String> col_deleted_phone;
    @FXML
    private TableColumn<DeletedEmployeeDetails, String> col_deleted_nic;
    @FXML
    private TableColumn<DeletedEmployeeDetails, String> col_deleted_date;
    @FXML
    private TableColumn<DeletedEmployeeDetails, String> col_deleted_address;
    
    // Search fields
    @FXML
    private TextField txt_search_id;
    @FXML
    private TextField txt_search_deleted_id;
    @FXML
    private Label lbl_search_deleted;
    
    // ScrollPanes
    @FXML
    private ScrollPane employeeTableScrollPane;
    @FXML
    private ScrollPane deletedEmployeeTableScrollPane;
    
    // Buttons
    @FXML
    private Button btn_delete_employee;
    @FXML
    private Button btn_history;
    @FXML
    private Button btn_undo_delete;
    @FXML
    private Button btn_back_to_main;

    // Initialize observable lists
    private ObservableList<EmployeeDetails> employeeData;
    private ObservableList<DeletedEmployeeDetails> deletedEmployeeData;

    // Database connection
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;
    
    // Current view state
    private boolean isHistoryView = false;
    private EmployeeDetails selectedEmployee = null;
    private DeletedEmployeeDetails selectedDeletedEmployee = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new DBHandler();
        
        // Initialize table columns for main employee table
        setupEmployeeTableColumns();
        
        // Initialize table columns for deleted employees table
        setupDeletedEmployeeTableColumns();
        
        // Load all employees initially (empty search shows all)
        loadEmployeeData("");
        
        // Set initial view state
        showMainView();
    }

    @FXML
    private void deleteEmployeeButtonAction(MouseEvent event) {
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(null, "Please select an employee to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete employee: " + selectedEmployee.getName() + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            deleteEmployee(selectedEmployee);
        }
    }

    private void setupEmployeeTableColumns() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("tel"));
        col_nic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        
        // Set column resize policy for better scrolling experience
        tableEmployee.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        
        // Set minimum widths for better readability
        col_id.setMinWidth(60);
        col_name.setMinWidth(160);
        col_email.setMinWidth(180);
        col_phonenumber.setMinWidth(140);
        col_nic.setMinWidth(170);
        col_address.setMinWidth(170);
        col_position.setMinWidth(160);
    }
    
    private void setupDeletedEmployeeTableColumns() {
        col_deleted_id.setCellValueFactory(new PropertyValueFactory<>("originalId"));
        col_deleted_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_deleted_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_deleted_phone.setCellValueFactory(new PropertyValueFactory<>("tel"));
        col_deleted_nic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_deleted_date.setCellValueFactory(new PropertyValueFactory<>("deletedDate"));
        col_deleted_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        // Set column resize policy for better scrolling experience
        tableDeletedEmployees.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        
        // Set minimum widths for better readability
        col_deleted_id.setMinWidth(60);
        col_deleted_name.setMinWidth(160);
        col_deleted_email.setMinWidth(180);
        col_deleted_phone.setMinWidth(140);
        col_deleted_nic.setMinWidth(170);
        col_deleted_date.setMinWidth(140);
        col_deleted_address.setMinWidth(220);
    }
    
    private void loadEmployeeData(String searchId) {
        connection = handler.connectDB();
        employeeData = FXCollections.observableArrayList();

        try {
            String query;
            if (searchId.isEmpty()) {
                query = "SELECT * FROM register_employee ORDER BY id";
                pst = connection.prepareStatement(query);
            } else {
                query = "SELECT * FROM register_employee WHERE id LIKE ? ORDER BY id";
                pst = connection.prepareStatement(query);
                pst.setString(1, "%" + searchId + "%");
            }
            
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String emgTelValue = rs.getObject("emg_tel") != null ? rs.getString("emg_tel") : "";
                employeeData.add(new EmployeeDetails(
                    rs.getString("id"), rs.getString("name"), rs.getString("emp_id"), 
                    rs.getString("email"), rs.getString("nic"), rs.getString("tel"), 
                    rs.getString("address"), rs.getString("department"), rs.getString("position"), emgTelValue));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading employee data: " + ex.getMessage());
        }

        tableEmployee.setItems(employeeData);
    }

    @FXML
    private void searchEmployeeAction(KeyEvent event) {
        String searchId = txt_search_id.getText().trim();
        loadEmployeeData(searchId);
        selectedEmployee = null; // Clear selection when searching
    }
    
    @FXML
    private void showHistoryAction(MouseEvent event) {
        if (isHistoryView) {
            showMainView();
        } else {
            showHistoryView();
        }
    }
    
    private void showMainView() {
        isHistoryView = false;
        // Show employee table scroll pane and hide deleted employees scroll pane
        employeeTableScrollPane.setVisible(true);
        deletedEmployeeTableScrollPane.setVisible(false);
        btn_delete_employee.setVisible(true);
        btn_undo_delete.setVisible(false);
        btn_back_to_main.setVisible(false);
        btn_history.setText("HISTORY");
        txt_search_id.setVisible(true);
        txt_search_deleted_id.setVisible(false);
        lbl_search_deleted.setVisible(false);
        loadEmployeeData(txt_search_id.getText().trim());
    }
    
    private void showHistoryView() {
        isHistoryView = true;
        // Hide employee table scroll pane and show deleted employees scroll pane
        employeeTableScrollPane.setVisible(false);
        deletedEmployeeTableScrollPane.setVisible(true);
        btn_delete_employee.setVisible(false);
        btn_undo_delete.setVisible(true);
        btn_back_to_main.setVisible(true);
        btn_history.setText("REFRESH");
        txt_search_id.setVisible(false);
        txt_search_deleted_id.setVisible(true);
        lbl_search_deleted.setVisible(true);
        loadDeletedEmployeeData("");
    }

    @FXML
    private void displaySelectedAction(MouseEvent event) {
        if (!isHistoryView) {
            selectedEmployee = tableEmployee.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                // Employee selected for deletion
                btn_delete_employee.setDisable(false);
            }
        } else {
            selectedDeletedEmployee = tableDeletedEmployees.getSelectionModel().getSelectedItem();
            if (selectedDeletedEmployee != null) {
                // Deleted employee selected for undo
                btn_undo_delete.setDisable(false);
            }
        }
    }

    private void deleteEmployee(EmployeeDetails employee) {
        connection = handler.connectDB();
        try {
            // Start transaction
            connection.setAutoCommit(false);
            
            // Insert into deleted_employees table
            String insertDeleted = "INSERT INTO deleted_employees (original_id, name, emp_id, email, nic, tel, address, department, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(insertDeleted);
            pst.setString(1, employee.getId());
            pst.setString(2, employee.getName());
            pst.setString(3, employee.getEmpId());
            pst.setString(4, employee.getEmail());
            pst.setString(5, employee.getNic());
            pst.setString(6, employee.getTel());
            pst.setString(7, employee.getAddress());
            pst.setString(8, employee.getDepartment());
            pst.setString(9, employee.getPosition());
            pst.executeUpdate();
            
            // Delete from register_employee table
            String deleteEmployee = "DELETE FROM register_employee WHERE id = ?";
            pst = connection.prepareStatement(deleteEmployee);
            pst.setString(1, employee.getId());
            pst.executeUpdate();
            
            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);
            
            JOptionPane.showMessageDialog(null, "Employee deleted successfully!");
            loadEmployeeData(txt_search_id.getText().trim());
            selectedEmployee = null;
            btn_delete_employee.setDisable(true);
            
        } catch (SQLException ex) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Error deleting employee: " + ex.getMessage());
        }
    }
    
    private void loadDeletedEmployeeData(String searchId) {
        connection = handler.connectDB();
        deletedEmployeeData = FXCollections.observableArrayList();

        try {
            String query;
            if (searchId.isEmpty()) {
                query = "SELECT * FROM deleted_employees ORDER BY deleted_date DESC";
                pst = connection.prepareStatement(query);
            } else {
                query = "SELECT * FROM deleted_employees WHERE original_id LIKE ? ORDER BY deleted_date DESC";
                pst = connection.prepareStatement(query);
                pst.setString(1, "%" + searchId + "%");
            }
            
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                deletedEmployeeData.add(new DeletedEmployeeDetails(
                    rs.getString("id"), rs.getString("original_id"), rs.getString("name"), 
                    rs.getString("emp_id"), rs.getString("email"), rs.getString("nic"), 
                    rs.getString("tel"), rs.getString("address"), rs.getString("department"), 
                    rs.getString("position"), rs.getString("deleted_date"), rs.getString("deleted_by")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading deleted employee data: " + ex.getMessage());
        }

        tableDeletedEmployees.setItems(deletedEmployeeData);
    }

    @FXML
    private void searchDeletedEmployeeAction(KeyEvent event) {
        String searchId = txt_search_deleted_id.getText().trim();
        loadDeletedEmployeeData(searchId);
        selectedDeletedEmployee = null; // Clear selection when searching
    }

    @FXML
    private void undoDeleteAction(MouseEvent event) {
        // Get the currently selected item from the deleted employees table
        selectedDeletedEmployee = tableDeletedEmployees.getSelectionModel().getSelectedItem();
        
        if (selectedDeletedEmployee == null) {
            JOptionPane.showMessageDialog(null, "Please select a deleted employee to restore!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to restore employee: " + selectedDeletedEmployee.getName() + "?", 
            "Confirm Restore", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            restoreEmployee(selectedDeletedEmployee);
        }
    }
    
    @FXML
    private void backToMainAction(MouseEvent event) {
        showMainView();
    }
    
    private void restoreEmployee(DeletedEmployeeDetails deletedEmployee) {
        connection = handler.connectDB();
        try {
            // Start transaction
            connection.setAutoCommit(false);
            
            // Insert back into register_employee table with original ID
            String insertEmployee = "INSERT INTO register_employee (id, name, emp_id, email, nic, tel, address, department, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(insertEmployee);
            pst.setString(1, deletedEmployee.getOriginalId());
            pst.setString(2, deletedEmployee.getName());
            pst.setString(3, deletedEmployee.getEmpId());
            pst.setString(4, deletedEmployee.getEmail());
            pst.setString(5, deletedEmployee.getNic());
            pst.setString(6, deletedEmployee.getTel());
            pst.setString(7, deletedEmployee.getAddress());
            pst.setString(8, deletedEmployee.getDepartment());
            pst.setString(9, deletedEmployee.getPosition());
            pst.executeUpdate();
            
            // Remove from deleted_employees table
            String removeDeleted = "DELETE FROM deleted_employees WHERE id = ?";
            pst = connection.prepareStatement(removeDeleted);
            pst.setString(1, deletedEmployee.getId());
            pst.executeUpdate();
            
            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);
            
            JOptionPane.showMessageDialog(null, "Employee restored successfully!");
            loadDeletedEmployeeData(txt_search_deleted_id.getText().trim());
            selectedDeletedEmployee = null;
            btn_undo_delete.setDisable(true);
            
        } catch (SQLException ex) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Error restoring employee: " + ex.getMessage());
        }
    }

}