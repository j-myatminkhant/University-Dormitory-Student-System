/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Employee;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import DBConnection.DBHandler;
import Model.EmployeeDetails;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author User
 */

public class All_Employee_LivingController implements Initializable {

    @FXML
    private TableView<EmployeeDetails> tableEmployee;
    @FXML
    private TableColumn<EmployeeDetails, String> col_id;
    @FXML
    private TableColumn<EmployeeDetails, String> col_name;
    @FXML
    private TableColumn<EmployeeDetails, String> col_nic;
    @FXML
    private TableColumn<EmployeeDetails, String> col_phonenumber;
    @FXML
    private TableColumn<EmployeeDetails, String> col_emgtel;
    @FXML
    private TableColumn<EmployeeDetails, String> col_address;
    @FXML
    private TableColumn<EmployeeDetails, String> col_position;
    @FXML
    private Button btn_back;

    /**
     * Initializes the controller class.
     */
    private ObservableList<EmployeeDetails> data;
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        handler = new DBHandler();
        loadEmployeeData();
    }
    
    private void loadEmployeeData() {
        connection = handler.connectDB();
        data = FXCollections.observableArrayList();

        try {
            // Execute query
            ResultSet rs = connection.createStatement().executeQuery("SELECT id, name, emp_id, email, nic, tel, address, department, position, emg_tel FROM register_employee");

            while (rs.next()) {
                // get string from db with all 10 parameters
                String emgTelValue = rs.getObject("emg_tel") != null ? rs.getString("emg_tel") : "";
                data.add(new EmployeeDetails(
                    rs.getString("id"), 
                    rs.getString("name"), 
                    rs.getString("emp_id"), 
                    rs.getString("email"),
                    rs.getString("nic"), 
                    rs.getString("tel"), 
                    rs.getString("address"), 
                    rs.getString("department"), 
                    rs.getString("position"),
                    emgTelValue
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        // set cell values
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_nic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("tel"));
        col_emgtel.setCellValueFactory(new PropertyValueFactory<>("emgTel"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_position.setCellValueFactory(new PropertyValueFactory<>("position"));

        tableEmployee.setItems(null);
        tableEmployee.setItems(data);
        
        // Auto-size columns to fit content
        autoSizeColumns();
    }
    
    private void autoSizeColumns() {
        // Auto-size columns to fit content
        tableEmployee.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Set minimum widths for better readability
        col_id.setMinWidth(60);
        col_name.setMinWidth(150);
        col_nic.setMinWidth(120);
        col_phonenumber.setMinWidth(120);
        col_emgtel.setMinWidth(120);
        col_address.setMinWidth(200);
        col_position.setMinWidth(120);
    }
    
    // Public method to refresh data (called from other controllers)
    public void refreshData() {
        loadEmployeeData();
    }    

    @FXML
    private void back_btn_clicked(MouseEvent event) throws IOException {
        // No action needed - this page is loaded within the main content area
        // The sidebar navigation handles page switching
    }
    
}
