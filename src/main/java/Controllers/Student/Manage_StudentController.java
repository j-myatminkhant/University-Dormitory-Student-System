/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import DBConnection.DatabaseUtility;

/**
 * FXML Controller class for Manage Student page
 *
 * @author User
 */
public class Manage_StudentController implements Initializable {

    @FXML private Button btn_add_student;
    @FXML private Button btn_update_student;
    @FXML private Button btn_delete_student;
    @FXML private Button btn_view_student;
    @FXML private Button btn_back;
    @FXML private Pane contentArea;
    @FXML private Label totalStudentsLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadStudentCount();
    }
    
    /**
     * Loads the total number of students from database
     */
    private void loadStudentCount() {
        try {
            String query = "SELECT COUNT(*) as total FROM register_students";
            ResultSet rs = DatabaseUtility.executeQuery(query);
            
            if (rs != null && rs.next()) {
                int count = rs.getInt("total");
                totalStudentsLabel.setText(String.valueOf(count));
            } else {
                totalStudentsLabel.setText("0");
            }
        } catch (Exception e) {
            System.err.println("Error loading student count: " + e.getMessage());
            e.printStackTrace();
            totalStudentsLabel.setText("0");
        }
    }

    @FXML
    private void addStudentAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Student/New_Student.fxml");
    }

    @FXML
    private void updateStudentAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Student/Update_Student.fxml");
    }

    @FXML
    private void deleteStudentAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Student/Delete_Student.fxml");
    }

    @FXML
    private void viewStudentAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Student/All_Student_Living.fxml");
    }

    @FXML
    private void back_btn_clicked(MouseEvent event) throws IOException {
        btn_back.getScene().getWindow().hide();
        
        Stage mainStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/MenuComponent.fxml"));
        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        mainStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setResizable(false);
    }
    
    /**
     * Loads a page into the content area
     */
    private void loadPage(String fxmlPath) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            
            // Clear existing content
            contentArea.getChildren().clear();
            
            // Add the new page content
            contentArea.getChildren().add(page);
            
            // Set the page to fit within the content area
            page.setLayoutX(0);
            page.setLayoutY(0);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Public method to load view students page automatically
     */
    public void loadViewStudentsPage() throws IOException {
        loadPage("/FXML/Student/All_Student_Living.fxml");
    }
}
