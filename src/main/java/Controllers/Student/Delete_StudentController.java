/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Student;

import DBConnection.DBHandler;
import Model.StudentDetails;
import Model.DeletedStudentDetails;
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
public class Delete_StudentController implements Initializable {

    // Main student table
    @FXML
    private TableView<StudentDetails> tableStudent;
    @FXML
    private TableColumn<StudentDetails, String> col_id;
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
    
    // Deleted students table
    @FXML
    private TableView<DeletedStudentDetails> tableDeletedStudents;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_id;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_name;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_nsbmid;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_email;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_phone;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_nic;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_date;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_address;
    @FXML
    private TableColumn<DeletedStudentDetails, String> col_deleted_room;
    
    // Search fields
    @FXML
    private TextField txt_search_id;
    @FXML
    private TextField txt_search_deleted_id;
    @FXML
    private Label lbl_search_deleted;
    
    // ScrollPanes
    @FXML
    private ScrollPane studentTableScrollPane;
    @FXML
    private ScrollPane deletedStudentTableScrollPane;
    
    // Buttons
    @FXML
    private Button btn_delete_student;
    @FXML
    private Button btn_history;
    @FXML
    private Button btn_undo_delete;
    @FXML
    private Button btn_back_to_main;

    // Initialize observable lists
    private ObservableList<StudentDetails> studentData;
    private ObservableList<DeletedStudentDetails> deletedStudentData;

    // Database connection
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;
    
    // Current view state
    private boolean isHistoryView = false;
    private StudentDetails selectedStudent = null;
    private DeletedStudentDetails selectedDeletedStudent = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new DBHandler();
        
        // Initialize table columns for main student table
        setupStudentTableColumns();
        
        // Initialize table columns for deleted students table
        setupDeletedStudentTableColumns();
        
        // Load all students initially (empty search shows all)
        loadStudentData("");
        
        // Set initial view state
        showMainView();
    }

    @FXML
    private void deleteStudentButtonAction(MouseEvent event) {
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(null, "Please select a student to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete student: " + selectedStudent.getName() + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            deleteStudent(selectedStudent);
        }
    }

    private void setupStudentTableColumns() {
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
    
    private void setupDeletedStudentTableColumns() {
        col_deleted_id.setCellValueFactory(new PropertyValueFactory<>("originalId"));
        col_deleted_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_deleted_nsbmid.setCellValueFactory(new PropertyValueFactory<>("nsbmId"));
        col_deleted_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_deleted_phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_deleted_nic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        col_deleted_date.setCellValueFactory(new PropertyValueFactory<>("deletedDate"));
        col_deleted_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_deleted_room.setCellValueFactory(new PropertyValueFactory<>("room"));
        
        // Set column resize policy for better scrolling experience
        tableDeletedStudents.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        
        // Set minimum widths for better readability
        col_deleted_id.setMinWidth(60);
        col_deleted_name.setMinWidth(120);
        col_deleted_nsbmid.setMinWidth(120);
        col_deleted_email.setMinWidth(150);
        col_deleted_phone.setMinWidth(120);
        col_deleted_nic.setMinWidth(150);
        col_deleted_date.setMinWidth(120);
        col_deleted_address.setMinWidth(200);
        col_deleted_room.setMinWidth(80);
    }
    
    private void loadStudentData(String searchId) {
        connection = handler.connectDB();
        studentData = FXCollections.observableArrayList();

        try {
            String query;
            if (searchId.isEmpty()) {
                query = "SELECT * FROM register_students ORDER BY id";
                pst = connection.prepareStatement(query);
            } else {
                query = "SELECT * FROM register_students WHERE id LIKE ? ORDER BY id";
                pst = connection.prepareStatement(query);
                pst.setString(1, "%" + searchId + "%");
            }
            
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                studentData.add(new StudentDetails(
                    rs.getString("id"), rs.getString("name"), rs.getString("nsbmID"), 
                    rs.getString("email"), rs.getString("phoneNumber"), rs.getString("nic"), 
                    rs.getString("address"), rs.getString("guardName"), rs.getString("guardTel"),
                    rs.getString("room")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading student data: " + ex.getMessage());
        }

        tableStudent.setItems(studentData);
    }

    @FXML
    private void searchStudentAction(KeyEvent event) {
        String searchId = txt_search_id.getText().trim();
        loadStudentData(searchId);
        selectedStudent = null; // Clear selection when searching
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
        // Show student table scroll pane and hide deleted students scroll pane
        studentTableScrollPane.setVisible(true);
        deletedStudentTableScrollPane.setVisible(false);
        btn_delete_student.setVisible(true);
        btn_undo_delete.setVisible(false);
        btn_back_to_main.setVisible(false);
        btn_history.setText("HISTORY");
        txt_search_id.setVisible(true);
        txt_search_deleted_id.setVisible(false);
        lbl_search_deleted.setVisible(false);
        loadStudentData(txt_search_id.getText().trim());
    }
    
    private void showHistoryView() {
        isHistoryView = true;
        // Hide student table scroll pane and show deleted students scroll pane
        studentTableScrollPane.setVisible(false);
        deletedStudentTableScrollPane.setVisible(true);
        btn_delete_student.setVisible(false);
        btn_undo_delete.setVisible(true);
        btn_back_to_main.setVisible(true);
        btn_history.setText("REFRESH");
        txt_search_id.setVisible(false);
        txt_search_deleted_id.setVisible(true);
        lbl_search_deleted.setVisible(true);
        loadDeletedStudentData("");
    }

    @FXML
    private void displaySelectedAction(MouseEvent event) {
        if (!isHistoryView) {
            selectedStudent = tableStudent.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                // Student selected for deletion
                btn_delete_student.setDisable(false);
            }
        } else {
            selectedDeletedStudent = tableDeletedStudents.getSelectionModel().getSelectedItem();
            if (selectedDeletedStudent != null) {
                // Deleted student selected for undo
                btn_undo_delete.setDisable(false);
            }
        }
    }

    private void deleteStudent(StudentDetails student) {
        connection = handler.connectDB();
        try {
            // Start transaction
            connection.setAutoCommit(false);
            
            // Insert into deleted_students table
            String insertDeleted = "INSERT INTO deleted_students (original_id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(insertDeleted);
            pst.setString(1, student.getId());
            pst.setString(2, student.getName());
            pst.setString(3, student.getNsbmId());
            pst.setString(4, student.getEmail());
            pst.setString(5, student.getPhoneNumber());
            pst.setString(6, student.getNic());
            pst.setString(7, student.getAddress());
            pst.setString(8, student.getGuardName());
            pst.setString(9, student.getGuardTel());
            pst.setString(10, student.getRoom());
            pst.executeUpdate();
            
            // Delete from register_students table
            String deleteStudent = "DELETE FROM register_students WHERE id = ?";
            pst = connection.prepareStatement(deleteStudent);
            pst.setString(1, student.getId());
            pst.executeUpdate();
            
            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);
            
            JOptionPane.showMessageDialog(null, "Student deleted successfully!");
            loadStudentData(txt_search_id.getText().trim());
            selectedStudent = null;
            btn_delete_student.setDisable(true);
            
        } catch (SQLException ex) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Error deleting student: " + ex.getMessage());
        }
    }
    
    private void loadDeletedStudentData(String searchId) {
        connection = handler.connectDB();
        deletedStudentData = FXCollections.observableArrayList();

        try {
            String query;
            if (searchId.isEmpty()) {
                query = "SELECT * FROM deleted_students ORDER BY deleted_date DESC";
                pst = connection.prepareStatement(query);
            } else {
                query = "SELECT * FROM deleted_students WHERE original_id LIKE ? ORDER BY deleted_date DESC";
                pst = connection.prepareStatement(query);
                pst.setString(1, "%" + searchId + "%");
            }
            
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                deletedStudentData.add(new DeletedStudentDetails(
                    rs.getString("id"), rs.getString("original_id"), rs.getString("name"), 
                    rs.getString("nsbmID"), rs.getString("email"), rs.getString("phoneNumber"), 
                    rs.getString("nic"), rs.getString("address"), rs.getString("guardName"), 
                    rs.getString("guardTel"), rs.getString("room"), 
                    rs.getString("deleted_date"), rs.getString("deleted_by")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading deleted student data: " + ex.getMessage());
        }

        tableDeletedStudents.setItems(deletedStudentData);
    }

    @FXML
    private void searchDeletedStudentAction(KeyEvent event) {
        String searchId = txt_search_deleted_id.getText().trim();
        loadDeletedStudentData(searchId);
        selectedDeletedStudent = null; // Clear selection when searching
    }

    @FXML
    private void undoDeleteAction(MouseEvent event) {
        // Get the currently selected item from the deleted students table
        selectedDeletedStudent = tableDeletedStudents.getSelectionModel().getSelectedItem();
        
        if (selectedDeletedStudent == null) {
            JOptionPane.showMessageDialog(null, "Please select a deleted student to restore!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to restore student: " + selectedDeletedStudent.getName() + "?", 
            "Confirm Restore", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            restoreStudent(selectedDeletedStudent);
        }
    }
    
    @FXML
    private void backToMainAction(MouseEvent event) {
        showMainView();
    }
    
    private void restoreStudent(DeletedStudentDetails deletedStudent) {
        connection = handler.connectDB();
        try {
            // Start transaction
            connection.setAutoCommit(false);
            
            // Insert back into register_students table with original ID
            String insertStudent = "INSERT INTO register_students (id, name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(insertStudent);
            pst.setString(1, deletedStudent.getOriginalId());
            pst.setString(2, deletedStudent.getName());
            pst.setString(3, deletedStudent.getNsbmId());
            pst.setString(4, deletedStudent.getEmail());
            pst.setString(5, deletedStudent.getPhoneNumber());
            pst.setString(6, deletedStudent.getNic());
            pst.setString(7, deletedStudent.getAddress());
            pst.setString(8, deletedStudent.getGuardName());
            pst.setString(9, deletedStudent.getGuardTel());
            pst.setString(10, deletedStudent.getRoom());
            pst.executeUpdate();
            
            // Remove from deleted_students table
            String removeDeleted = "DELETE FROM deleted_students WHERE id = ?";
            pst = connection.prepareStatement(removeDeleted);
            pst.setString(1, deletedStudent.getId());
            pst.executeUpdate();
            
            // Commit transaction
            connection.commit();
            connection.setAutoCommit(true);
            
            JOptionPane.showMessageDialog(null, "Student restored successfully!");
            loadDeletedStudentData(txt_search_deleted_id.getText().trim());
            selectedDeletedStudent = null;
            btn_undo_delete.setDisable(true);
            
        } catch (SQLException ex) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Error restoring student: " + ex.getMessage());
        }
    }

}
