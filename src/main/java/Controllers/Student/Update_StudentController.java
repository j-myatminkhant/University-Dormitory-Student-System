/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Student;

import Model.StudentDetails;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import DBConnection.DBHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
public class Update_StudentController implements Initializable {

    @FXML
    private TextField reg_txt_username;
    @FXML
    private TextField reg_txt_nsbmid;
    @FXML
    private TextField reg_txt_email;
    @FXML
    private TextField reg_txt_phnmb;
    @FXML
    private TextField reg_txt_nic;
    @FXML
    private TextField reg_txt_address;
    @FXML
    private TextField reg_txt_guardname;
    @FXML
    private TextField reg_txt_guardtel;
    @FXML
    private TextField reg_txt_room;
    @FXML
    private Button btn_update_student;

    @FXML
    private Button btn_refersh;
    // Initialize observable list to database
    private ObservableList<StudentDetails> data;

    @FXML
    private TableView<StudentDetails> tableStudent;
    @FXML
    private TableColumn<StudentDetails, String> col_name;
    @FXML
    private TableColumn<StudentDetails, String> col_nsbmid;
    @FXML
    private TableColumn<StudentDetails, String> col_email;
    @FXML
    private TableColumn<StudentDetails, String> col_phonenumber;
    @FXML
    private TableColumn<StudentDetails, String> col_nic;
    @FXML
    private TableColumn<StudentDetails, String> col_address;
    @FXML
    private TableColumn<StudentDetails, String> col_g_name;
    @FXML
    private TableColumn<StudentDetails, String> col_g_tel;
    @FXML
    private TableColumn<StudentDetails, String> col_room;

    @FXML
    private ScrollPane studentTableScrollPane;

    /**
     * Initializes the controller class.
     */
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;

    @FXML
    private TextField reg_txt_id;
    @FXML
    private TableColumn<StudentDetails, String> col_id;
    @FXML
    private Button btn_back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        handler = new DBHandler();

        // Automatically load student data when the page opens
        autoRefresh();
    }

    private void autoRefresh() {
        connection = handler.connectDB();
        data = FXCollections.observableArrayList();

        try {
            // Execute query with specific columns to avoid timestamp confusion
            ResultSet rs = connection.createStatement().executeQuery("SELECT id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room FROM register_students");

            while (rs.next()) {
                // get string from db using column names for clarity
                String roomValue = rs.getObject("room") != null ? rs.getString("room") : "";
                data.add(new StudentDetails(
                    rs.getString("id"), 
                    rs.getString("name"), 
                    rs.getString("nsbmID"), 
                    rs.getString("email"),
                    rs.getString("phoneNumber"), 
                    rs.getString("nic"), 
                    rs.getString("address"), 
                    rs.getString("guardName"), 
                    rs.getString("guardTel"),
                    roomValue));

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        // set cell values
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_nsbmid.setCellValueFactory(new PropertyValueFactory<>("nsbmId"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_nic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_g_name.setCellValueFactory(new PropertyValueFactory<>("guardName"));
        col_g_tel.setCellValueFactory(new PropertyValueFactory<>("guardTel"));
        col_room.setCellValueFactory(new PropertyValueFactory<>("room"));

        tableStudent.setItems(null);
        tableStudent.setItems(data);

        // Auto-size columns to fit content
        autoSizeColumns();
    }

    @FXML
    private void updateStudentButtonAction(MouseEvent event) {
        String id = reg_txt_id.getText();
        String userName = reg_txt_username.getText();
        String nsbmID = reg_txt_nsbmid.getText();
        String email = reg_txt_email.getText();
        String phoneNumber = reg_txt_phnmb.getText();
        String nic = reg_txt_nic.getText();
        String address = reg_txt_address.getText();
        String guardName = reg_txt_guardname.getText();
        String guardTel = reg_txt_guardtel.getText();
        String room = reg_txt_room.getText();

        if (userName.equals("")
                || nsbmID.equals("")
                || email.equals("")
                || phoneNumber.equals("")
                || nic.equals("")
                || address.equals("")
                || guardName.equals("")
                || guardTel.equals("")
                || room.equals("")) {
            JOptionPane.showMessageDialog(null, "Some Fields are missing!");
        } else {
            String update = "UPDATE register_Students set id = ?, name = ?,nsbmID = ?,email = ?,phoneNumber = ?,nic = ?,address = ?,guardName = ?,guardTel = ?,room = ? where id = '"
                    + id + "' ";
            connection = handler.connectDB();
            try {
                pst = connection.prepareStatement(update);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                pst.setString(1, reg_txt_id.getText());
                pst.setString(2, reg_txt_username.getText());
                pst.setString(3, reg_txt_nsbmid.getText());
                pst.setString(4, reg_txt_email.getText());
                pst.setString(5, reg_txt_phnmb.getText());
                pst.setString(6, reg_txt_nic.getText());
                pst.setString(7, reg_txt_address.getText());
                pst.setString(8, reg_txt_guardname.getText());
                pst.setString(9, reg_txt_guardtel.getText());
                // Handle room field properly as integer
                String roomText = reg_txt_room.getText().trim();
                if (roomText.isEmpty()) {
                    pst.setNull(10, java.sql.Types.INTEGER);
                } else {
                    try {
                        pst.setInt(10, Integer.parseInt(roomText));
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Room must be a valid number!");
                        return;
                    }
                }

                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Update Student!!");
                autoRefresh();
            } catch (SQLException ex) {
                Logger.getLogger(New_StudentController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private void autoSizeColumns() {
        // Set column resize policy for better scrolling experience
        tableStudent.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Set minimum widths for better readability
        col_id.setMinWidth(60);
        col_name.setMinWidth(120);
        col_nsbmid.setMinWidth(120);
        col_email.setMinWidth(150);
        col_phonenumber.setMinWidth(120);
        col_nic.setMinWidth(150);
        col_address.setMinWidth(150);
        col_g_name.setMinWidth(120);
        col_g_tel.setMinWidth(120);
        col_room.setMinWidth(80);
    }

    @FXML
    private void refreshButtionClickAction(MouseEvent event) {
        // Clear all text fields
        reg_txt_id.clear();
        reg_txt_username.clear();
        reg_txt_nsbmid.clear();
        reg_txt_phnmb.clear();
        reg_txt_nic.clear();
        reg_txt_email.clear();
        reg_txt_address.clear();
        reg_txt_guardname.clear();
        reg_txt_guardtel.clear();
        reg_txt_room.clear();
        
        // Refresh the table data
        connection = handler.connectDB();
        data = FXCollections.observableArrayList();

        try {
            // Execute query with specific columns to avoid timestamp confusion
            ResultSet rs = connection.createStatement().executeQuery("SELECT id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room FROM register_students");

            while (rs.next()) {
                // get string from db using column names for clarity
                String roomValue = rs.getObject("room") != null ? rs.getString("room") : "";
                data.add(new StudentDetails(
                    rs.getString("id"), 
                    rs.getString("name"), 
                    rs.getString("nsbmID"), 
                    rs.getString("email"),
                    rs.getString("phoneNumber"), 
                    rs.getString("nic"), 
                    rs.getString("address"), 
                    rs.getString("guardName"), 
                    rs.getString("guardTel"),
                    roomValue));

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        // set cell values
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_nsbmid.setCellValueFactory(new PropertyValueFactory<>("nsbmId"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phonenumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_nic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_g_name.setCellValueFactory(new PropertyValueFactory<>("guardName"));
        col_g_tel.setCellValueFactory(new PropertyValueFactory<>("guardTel"));
        col_room.setCellValueFactory(new PropertyValueFactory<>("room"));

        tableStudent.setItems(null);
        tableStudent.setItems(data);
    }

    @FXML
    private void displaySelectedAction(MouseEvent event) {
        StudentDetails student = tableStudent.getSelectionModel().getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(null, "Nothing Selected!");
        } else {
            String id = student.getId();
            String name = student.getName();
            String nsbmid = student.getNsbmId();
            String email = student.getEmail();
            String phonenumber = student.getPhoneNumber();
            String nic = student.getNIC();
            String address = student.getAddress();
            String g_name = student.getGuardName();
            String g_tel = student.getGuardTel();
            String room = student.getRoom();

            reg_txt_id.setText(id);
            reg_txt_username.setText(name);
            reg_txt_nsbmid.setText(nsbmid);
            reg_txt_email.setText(email);
            reg_txt_phnmb.setText(phonenumber);
            reg_txt_nic.setText(nic);
            reg_txt_address.setText(address);
            reg_txt_guardname.setText(g_name);
            reg_txt_guardtel.setText(g_tel);
            reg_txt_room.setText(room);
        }
    }

    @FXML
    private void back_btn_clicked(MouseEvent event) throws IOException {
        btn_back.getScene().getWindow().hide();

        Stage stu_Menu = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Student/Student_Menu.fxml"));
        Scene scene = new Scene(root);
        stu_Menu.setScene(scene);
        stu_Menu.initStyle(StageStyle.TRANSPARENT);
        stu_Menu.show();
        stu_Menu.setResizable(false);

    }

}
