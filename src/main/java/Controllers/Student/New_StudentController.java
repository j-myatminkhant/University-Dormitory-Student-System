/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Student;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;

import DBConnection.DBHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.pdfbox.util.Hex;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import java.util.regex.Pattern;

/**
 * FXML Controller class
 *
 * @author User
 */
public class New_StudentController implements Initializable {

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
    private AnchorPane roomSelectionPane;
    @FXML
    private GridPane roomGrid;
    @FXML
    private Button btn_choose_room;
    @FXML
    private Button btn_back_to_form;

    /**
     * Initializes the controller class.
     */
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;
    @FXML
    private Button btn_back;
    @FXML
    private Button btn_reg_student;
    @FXML
    private Button btn_cam_open;

    // Room selection variables
    private Button selectedRoomButton = null;
    private int selectedRoomNumber = -1;
    private int registeredStudentId = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        handler = new DBHandler();

        // Add validation listeners
        setupValidationListeners();
    }

    private void setupValidationListeners() {
        // Name validation - only alphabetic characters
        reg_txt_username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[a-zA-Z\\s]*$")) {
                setTextFieldError(reg_txt_username, "Only alphabet characters are allowed");
            } else {
                clearTextFieldError(reg_txt_username);
            }
        });

        // Thanata No validation - exactly 4 digits
        reg_txt_nsbmid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d{0,4}$")) {
                setTextFieldError(reg_txt_nsbmid, "Thanata No. must be 4 digits only");
            } else {
                clearTextFieldError(reg_txt_nsbmid);
            }
        });

        // Email validation
        reg_txt_email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidEmail(newValue)) {
                setTextFieldError(reg_txt_email, "Invalid email format");
            } else {
                clearTextFieldError(reg_txt_email);
            }
        });

        // NRC validation
        reg_txt_nic.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidNRC(newValue)) {
                setTextFieldError(reg_txt_nic, "Invalid NRC format. Example: 7/PMN(N)111111");
            } else {
                clearTextFieldError(reg_txt_nic);
            }
        });

        // Phone number validation
        reg_txt_phnmb.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^0\\d{8}(\\d{2})?$")) {
                setTextFieldError(reg_txt_phnmb, "Phone number must be 9 or 11 digits");
            } else {
                clearTextFieldError(reg_txt_phnmb);
            }
        });

        // Guardian phone validation
        reg_txt_guardtel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^0\\d{8}(\\d{2})?$")) {
                setTextFieldError(reg_txt_guardtel, "Phone number must be 9 or 11 digits");
            } else {
                clearTextFieldError(reg_txt_guardtel);
            }
        });
    }

    /*
     * private boolean isValidEmail(String email) {
     * String emailRegex =
     * "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.com$";
     * Pattern pattern = Pattern.compile(emailRegex);
     * return pattern.matcher(email).matches();
     * }
     */

    private static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        email = email.trim();

        // Fully escaped for Java
        String emailRegex = "^(?!.*\\.\\.)[a-z][a-z0-9.]*" +
                "(?<!\\.)@(gmail\\.com|uit\\.edu\\.mm)$";

        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidNRC(String nrc) {
        String nrcRegex = "^(?:[1-9]|1[0-4])/[A-Z]{3}\\(N\\)\\d{6}$";
        Pattern pattern = Pattern.compile(nrcRegex);
        return pattern.matcher(nrc).matches();
    }

    private void setTextFieldError(TextField textField, String errorMessage) {
        textField.setStyle(
                "-fx-background-radius: 10px; -fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 10px;");
        textField.setPromptText(errorMessage);
    }

    private void clearTextFieldError(TextField textField) {
        textField.setStyle("-fx-background-radius: 10px;");
        // Restore original prompt text
        if (textField == reg_txt_username) {
            textField.setPromptText("Name");
        } else if (textField == reg_txt_nsbmid) {
            textField.setPromptText("Thanata No");
        } else if (textField == reg_txt_email) {
            textField.setPromptText("abc@gmail.com");
        } else if (textField == reg_txt_nic) {
            textField.setPromptText("NRC No (e.g., 7/PMN(N)111111)");
        } else if (textField == reg_txt_phnmb) {
            textField.setPromptText("Contact No");
        } else if (textField == reg_txt_guardtel) {
            textField.setPromptText("Guardian Tele No");
        }
    }

    @FXML
    private void registerButtonAction(MouseEvent event) {
        String userName = reg_txt_username.getText();
        String nsbmID = reg_txt_nsbmid.getText();
        String email = reg_txt_email.getText();
        String phoneNumber = reg_txt_phnmb.getText();
        String nic = reg_txt_nic.getText();
        String address = reg_txt_address.getText();
        String guardName = reg_txt_guardname.getText();
        String guardTel = reg_txt_guardtel.getText();

        // Validation checks
        if (userName.equals("") || nsbmID.equals("") || email.equals("") || phoneNumber.equals("")
                || nic.equals("") || address.equals("") || guardName.equals("") || guardTel.equals("")) {
            JOptionPane.showMessageDialog(null, "All Fields Are Required!");
            return;
        }

        // Validate name (only alphabetic characters)
        if (!userName.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(null, "Name must contain only alphabetic characters!");
            return;
        }

        // Validate Thanata No (exactly 4 digits)
        if (!nsbmID.matches("^\\d{4}$")) {
            JOptionPane.showMessageDialog(null, "Thanata No. must be exactly 4 digits!");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email format!");
            return;
        }

        // Validate NRC format
        if (!isValidNRC(nic)) {
            JOptionPane.showMessageDialog(null, "Invalid NRC format. Example: 7/PMN(N)111111");
            return;
        }

        // Validate phone numbers (9 or 11 digits)
        if (!phoneNumber.matches("^0\\d{8}(\\d{2})?$")) {
            JOptionPane.showMessageDialog(null, "Student phone number must be 9 or 11 digits!");
            return;
        }

        if (!guardTel.matches("^0\\d{8}(\\d{2})?$")) {
            JOptionPane.showMessageDialog(null, "Guardian phone number must be 9 or 11 digits!");
            return;
        }

        // All validations passed, proceed with registration
        String insert = "INSERT INTO register_Students(name, nsbmID, email, phoneNumber, nic, address, guardName, guardTel, room)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, NULL)";
        connection = handler.connectDB();
        try {
            pst = connection.prepareStatement(insert);
            pst.setString(1, reg_txt_username.getText());
            pst.setString(2, reg_txt_nsbmid.getText());
            pst.setString(3, reg_txt_email.getText());
            pst.setString(4, reg_txt_phnmb.getText());
            pst.setString(5, reg_txt_nic.getText());
            pst.setString(6, reg_txt_address.getText());
            pst.setString(7, reg_txt_guardname.getText());
            pst.setString(8, reg_txt_guardtel.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Student Registered Successfully!");

            // Store the registered student ID for room assignment
            String getIdQuery = "SELECT LAST_INSERT_ID() as id";
            PreparedStatement getIdStmt = connection.prepareStatement(getIdQuery);
            java.sql.ResultSet rs = getIdStmt.executeQuery();
            if (rs.next()) {
                registeredStudentId = rs.getInt("id");
            }

            // Show room selection in the same window
            showRoomSelection();

        } catch (SQLException ex) {
            Logger.getLogger(New_StudentController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }

    private void redirectToLiveInStudents() throws IOException {
        btn_reg_student.getScene().getWindow().hide();

        Stage roomSelectionStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Student/Room_Selection.fxml"));
        Scene scene = new Scene(root);
        roomSelectionStage.initStyle(StageStyle.TRANSPARENT);
        roomSelectionStage.setScene(scene);
        roomSelectionStage.show();
        roomSelectionStage.setResizable(false);

        // Add mouse drag functionality
        root.setOnMousePressed(event -> {
            double xoffset = event.getSceneX();
            double yoffset = event.getSceneY();
            root.setOnMouseDragged(dragEvent -> {
                roomSelectionStage.setX(dragEvent.getScreenX() - xoffset);
                roomSelectionStage.setY(dragEvent.getScreenY() - yoffset);
            });
        });
    }

    @FXML
    private void back_btn_clicked(MouseEvent event) throws IOException {
        btn_back.getScene().getWindow().hide();

        Stage stu_Menu = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Student/Student_Menu.fxml"));
        Scene scene = new Scene(root);
        stu_Menu.initStyle(StageStyle.TRANSPARENT);
        stu_Menu.setScene(scene);
        stu_Menu.show();
        stu_Menu.setResizable(false);
    }

    @FXML
    private void cam_open_clicked(MouseEvent event) throws IOException {

        Camera camera = new Camera();
        String id = camera.camera_Open();
        reg_txt_nsbmid.setText(id);
        String name = camera.scan();
        reg_txt_username.setText(name);
    }

    @FXML
    private void setTExtRefresh() {
        reg_txt_username.setText("");
        reg_txt_nsbmid.setText("");
        reg_txt_email.setText("");
        reg_txt_phnmb.setText("");
        reg_txt_nic.setText("");
        reg_txt_address.setText("");
        reg_txt_guardname.setText("");
        reg_txt_guardtel.setText("");
    }

    private void showRoomSelection() {
        roomSelectionPane.setVisible(true);
        createRoomGrid();
    }

    @FXML
    private void backToFormAction(MouseEvent event) {
        roomSelectionPane.setVisible(false);
        selectedRoomButton = null;
        selectedRoomNumber = -1;
    }

    private void createRoomGrid() {
        try {
            roomGrid.getChildren().clear();

            // Get all occupied rooms from database
            connection = handler.connectDB();
            String query = "SELECT room FROM register_students WHERE room IS NOT NULL";
            java.sql.ResultSet rs = connection.createStatement().executeQuery(query);

            // Create a set of occupied room numbers
            java.util.Set<Integer> occupiedRooms = new java.util.HashSet<>();
            while (rs.next()) {
                occupiedRooms.add(rs.getInt("room"));
            }

            // Create 300 room buttons in 15 columns x 20 rows
            int roomNumber = 1;
            for (int row = 0; row < 20; row++) {
                for (int col = 0; col < 15; col++) {
                    Button roomButton = new Button(String.valueOf(roomNumber));
                    roomButton.setPrefHeight(35.0);
                    roomButton.setPrefWidth(45.0);

                    if (occupiedRooms.contains(roomNumber)) {
                        // Occupied room - red
                        roomButton.setStyle(
                                "-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-weight: bold; -fx-font-size: 12px;");
                        roomButton.setDisable(true);
                    } else {
                        // Available room - green
                        roomButton.setStyle(
                                "-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-weight: bold; -fx-font-size: 12px;");
                    }

                    // Add click handler for available rooms
                    final int currentRoomNumber = roomNumber;
                    if (!occupiedRooms.contains(roomNumber)) {
                        roomButton.setOnMouseClicked(event -> selectRoom(roomButton, currentRoomNumber));
                    }

                    roomGrid.add(roomButton, col, row);
                    roomNumber++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading room data: " + e.getMessage());
        }
    }

    private void selectRoom(Button roomButton, int roomNumber) {
        // Reset previous selection
        if (selectedRoomButton != null && !selectedRoomButton.isDisabled()) {
            selectedRoomButton.setStyle(
                    "-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-weight: bold; -fx-font-size: 12px;");
        }

        // Set new selection
        selectedRoomButton = roomButton;
        selectedRoomNumber = roomNumber;
        roomButton.setStyle(
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-weight: bold; -fx-font-size: 12px;");
    }

    @FXML
    private void chooseRoomAction(MouseEvent event) {
        if (selectedRoomNumber == -1) {
            JOptionPane.showMessageDialog(null, "Please select a room before proceeding.");
            return;
        }

        try {
            connection = handler.connectDB();

            // Check if room is already occupied
            String checkQuery = "SELECT id FROM register_students WHERE room = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, selectedRoomNumber);
            java.sql.ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Room " + selectedRoomNumber + " is already occupied. Please select another room.");
                return;
            }

            // Assign the room to the registered student
            String updateQuery = "UPDATE register_students SET room = ? WHERE id = ?";
            pst = connection.prepareStatement(updateQuery);
            pst.setInt(1, selectedRoomNumber);
            pst.setInt(2, registeredStudentId);
            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Room " + selectedRoomNumber + " has been assigned successfully!");

                // Clear form and navigate to view students page
                setTExtRefresh();
                roomSelectionPane.setVisible(false);
                selectedRoomButton = null;
                selectedRoomNumber = -1;
                registeredStudentId = -1;

                // Navigate to view students page in main application
                navigateToViewStudents();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to assign room: " + e.getMessage());
        }
    }

    private void navigateToViewStudents() {
        try {
            // Close current registration window
            btn_reg_student.getScene().getWindow().hide();

            // Open main student management window with view students page loaded
            Stage studentManageStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Student/Manage_Student.fxml"));
            Parent root = loader.load();

            // Get the controller and load the view students page
            Manage_StudentController controller = loader.getController();
            controller.loadViewStudentsPage();

            Scene scene = new Scene(root);
            studentManageStage.setScene(scene);
            studentManageStage.initStyle(StageStyle.TRANSPARENT);
            studentManageStage.show();
            studentManageStage.setResizable(false);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error navigating to view students: " + e.getMessage());
        }
    }

}
