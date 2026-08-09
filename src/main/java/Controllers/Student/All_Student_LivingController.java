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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import DBConnection.DBHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class All_Student_LivingController implements Initializable {

    // Initialize observable list to database
    private ObservableList<StudentDetails> data;

    @FXML
    private TableView<StudentDetails> tableStudent;
    @FXML
    private TableColumn<StudentDetails, String> col_id1;
    @FXML
    private TableColumn<StudentDetails, String> col_name1;
    @FXML
    private TableColumn<StudentDetails, String> col_nsbmid1;
    @FXML
    private TableColumn<StudentDetails, String> col_email1;
    @FXML
    private TableColumn<StudentDetails, String> col_phonenumber1;
    @FXML
    private TableColumn<StudentDetails, String> col_nic1;
    @FXML
    private TableColumn<StudentDetails, String> col_address1;
    @FXML
    private TableColumn<StudentDetails, String> col_g_name1;
    @FXML
    private TableColumn<StudentDetails, String> col_g_tel1;
    @FXML
    private TableColumn<StudentDetails, String> col_room1;

    /**
     * Initializes the controller class.
     */
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;
    @FXML
    private Button btn_back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        handler = new DBHandler();
        connection = handler.connectDB();
        data = FXCollections.observableArrayList();

        try {
            // Execute query with explicit column selection
            String query = "SELECT id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, " +
                         "COALESCE(room, 'Not Assigned') as room " +
                         "FROM register_students";
            ResultSet rs = connection.createStatement().executeQuery(query);

            while (rs.next()) {
                // Get values by column name for clarity and safety
                String id = rs.getString("id");
                String name = rs.getString("name");
                String nsbmID = rs.getString("nsbmID");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String nic = rs.getString("nic");
                String address = rs.getString("address");
                String guardName = rs.getString("guardName");
                String guardTel = rs.getString("guardTel");
                String room = rs.getString("room");
                
                data.add(new StudentDetails(id, name, nsbmID, email, phoneNumber, nic, 
                                         address, guardName, guardTel, room));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        // set cell values
        col_id1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_nsbmid1.setCellValueFactory(new PropertyValueFactory<>("nsbmId"));
        col_email1.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phonenumber1.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_nic1.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_address1.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_g_name1.setCellValueFactory(new PropertyValueFactory<>("guardName"));
        col_g_tel1.setCellValueFactory(new PropertyValueFactory<>("guardTel"));
        col_room1.setCellValueFactory(new PropertyValueFactory<>("room"));

        tableStudent.setItems(null);
        tableStudent.setItems(data);
        
        // Auto-size columns to fit content
        autoSizeColumns();
    }

    private void autoSizeColumns() {
        // Auto-size columns to fit content
        tableStudent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Set minimum widths for better readability
        col_id1.setMinWidth(60);
        col_name1.setMinWidth(120);
        col_nsbmid1.setMinWidth(120);
        col_email1.setMinWidth(150);
        col_phonenumber1.setMinWidth(120);
        col_nic1.setMinWidth(100);
        col_address1.setMinWidth(150);
        col_g_name1.setMinWidth(120);
        col_g_tel1.setMinWidth(120);
        col_room1.setMinWidth(100);
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
