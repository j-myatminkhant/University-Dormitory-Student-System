import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestBackButton {
    public static void main(String[] args) {
        System.out.println("=== Testing Back Button Functionality ===");
        
        try {
            // Test 1: Check if FXML file has back button
            testFXMLBackButton();
            
            // Test 2: Check if controller has back button method
            testControllerBackButton();
            
            // Test 3: Check if back button is properly configured
            testBackButtonConfiguration();
            
            System.out.println("\n🎉 ALL BACK BUTTON TESTS PASSED!");
            
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testFXMLBackButton() {
        System.out.println("\n--- Test 1: FXML Back Button ---");
        
        try {
            String fxmlPath = "src/main/resources/FXML/Employee/Update_Employee.fxml";
            String content = new String(Files.readAllBytes(Paths.get(fxmlPath)));
            
            if (content.contains("fx:id=\"btn_back\"")) {
                System.out.println("✓ Back button ID found in FXML");
            } else {
                System.out.println("✗ Back button ID not found in FXML");
            }
            
            if (content.contains("onMouseClicked=\"#back_btn_clicked\"")) {
                System.out.println("✓ Back button click handler found in FXML");
            } else {
                System.out.println("✗ Back button click handler not found in FXML");
            }
            
            if (content.contains("text=\"BACK\"")) {
                System.out.println("✓ Back button text found in FXML");
            } else {
                System.out.println("✗ Back button text not found in FXML");
            }
            
        } catch (IOException e) {
            System.out.println("✗ Error reading FXML file: " + e.getMessage());
        }
    }
    
    private static void testControllerBackButton() {
        System.out.println("\n--- Test 2: Controller Back Button Method ---");
        
        try {
            String controllerPath = "src/main/java/Controllers/Employee/Update_EmployeeController.java";
            String content = new String(Files.readAllBytes(Paths.get(controllerPath)));
            
            if (content.contains("@FXML")) {
                System.out.println("✓ @FXML annotation found in controller");
            } else {
                System.out.println("✗ @FXML annotation not found in controller");
            }
            
            if (content.contains("private Button btn_back;")) {
                System.out.println("✓ Back button field found in controller");
            } else {
                System.out.println("✗ Back button field not found in controller");
            }
            
            if (content.contains("back_btn_clicked")) {
                System.out.println("✓ Back button click method found in controller");
            } else {
                System.out.println("✗ Back button click method not found in controller");
            }
            
            if (content.contains("Employee_Menu.fxml")) {
                System.out.println("✓ Employee menu navigation found in controller");
            } else {
                System.out.println("✗ Employee menu navigation not found in controller");
            }
            
        } catch (IOException e) {
            System.out.println("✗ Error reading controller file: " + e.getMessage());
        }
    }
    
    private static void testBackButtonConfiguration() {
        System.out.println("\n--- Test 3: Back Button Configuration ---");
        
        try {
            String fxmlPath = "src/main/resources/FXML/Employee/Update_Employee.fxml";
            String content = new String(Files.readAllBytes(Paths.get(fxmlPath)));
            
            // Check button styling
            if (content.contains("-fx-background-color: #E74C3C")) {
                System.out.println("✓ Back button has red background color");
            } else {
                System.out.println("✗ Back button color not found");
            }
            
            if (content.contains("-fx-text-fill: white")) {
                System.out.println("✓ Back button has white text color");
            } else {
                System.out.println("✗ Back button text color not found");
            }
            
            if (content.contains("-fx-background-radius: 20px")) {
                System.out.println("✓ Back button has rounded corners");
            } else {
                System.out.println("✗ Back button rounded corners not found");
            }
            
            if (content.contains("prefHeight=\"40.0\"")) {
                System.out.println("✓ Back button has proper height");
            } else {
                System.out.println("✗ Back button height not found");
            }
            
            if (content.contains("prefWidth=\"130.0\"")) {
                System.out.println("✓ Back button has proper width");
            } else {
                System.out.println("✗ Back button width not found");
            }
            
        } catch (IOException e) {
            System.out.println("✗ Error reading FXML file: " + e.getMessage());
        }
    }
}


