/*
 * Test Update Employee FXML Loading - Fixed Version
 */

import Controllers.Employee.Update_EmployeeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestUpdateEmployeeFXML extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("=== Testing Update Employee FXML (FIXED) ===");
            
            // Load Update Employee FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Employee/Update_Employee.fxml"));
            Parent root = loader.load();
            
            System.out.println("✅ FXML loaded successfully - ScrollPane import fixed!");
            
            // Get controller instance
            Update_EmployeeController controller = loader.getController();
            if (controller != null) {
                System.out.println("✅ Controller initialized successfully");
            }
            
            // Create and show the scene
            Scene scene = new Scene(root);
            primaryStage.setTitle("Update Employee - FIXED");
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.show();
            primaryStage.centerOnScreen();
            primaryStage.setResizable(true);
            
            System.out.println("✅ Update Employee window opened successfully!");
            System.out.println("✅ ScrollPane error has been FIXED!");
            System.out.println("\n📋 The window should now display:");
            System.out.println("1. Employee form fields at the top");
            System.out.println("2. Employee table in a scrollable area");
            System.out.println("3. UPDATE, REFRESH, and BACK buttons");
            System.out.println("4. No more FXML errors!");
            
        } catch (Exception ex) {
            System.out.println("❌ Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing Update Employee FXML Fix...");
        launch(args);
    }
}
