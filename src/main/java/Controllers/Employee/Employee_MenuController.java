/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Employee;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import DBConnection.DatabaseUtility;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Employee_MenuController implements Initializable {
    
    double xoffset, yoffset;

    @FXML private Button btn_add_employee;
    @FXML private Button btn_update_employee;
    @FXML private Button btn_delete_employee;
    @FXML private Button btn_view_employee;
    @FXML private Button btn_back;
    @FXML private Pane contentArea;
    @FXML private Label totalEmployeesLabel;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadEmployeeCount();
    }
    
    /**
     * Loads the total number of employees from database
     */
    private void loadEmployeeCount() {
        try {
            String query = "SELECT COUNT(*) as total FROM register_employee";
            ResultSet rs = DatabaseUtility.executeQuery(query);
            
            if (rs != null && rs.next()) {
                int count = rs.getInt("total");
                totalEmployeesLabel.setText(String.valueOf(count));
            } else {
                totalEmployeesLabel.setText("0");
            }
        } catch (Exception e) {
            System.err.println("Error loading employee count: " + e.getMessage());
            e.printStackTrace();
            totalEmployeesLabel.setText("0");
        }
    }    

    @FXML
    private void addEmployeeAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Employee/New_Employee.fxml");
    }

    @FXML
    private void updateEmployeeAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Employee/Update_Employee.fxml");
    }

    @FXML
    private void deleteEmployeeAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Employee/Delete_Employee.fxml");
    }

    @FXML
    private void viewEmployeeAction(MouseEvent event) throws IOException {
        loadPage("/FXML/Employee/All_Employee_Living.fxml");
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
     * Public method to load view employees page automatically
     */
    public void loadViewEmployeesPage() throws IOException {
        loadPage("/FXML/Employee/All_Employee_Living.fxml");
    }



    @FXML
    private void back_btn_clicked(MouseEvent event) throws IOException {
        btn_back.getScene().getWindow().hide();

        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/MenuComponent.fxml"));
        Scene scene = new Scene(root);
        login.initStyle(StageStyle.TRANSPARENT);
        login.setScene(scene);
        login.show();
        login.setResizable(false);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                xoffset = event.getSceneX();
                yoffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                login.setX(event.getScreenX() - xoffset);
                login.setY(event.getScreenY() - yoffset);
            }
        });
    }

}
