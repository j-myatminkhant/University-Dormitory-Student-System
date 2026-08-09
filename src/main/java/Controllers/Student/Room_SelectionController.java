package Controllers.Student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import DBConnection.DBHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Room_SelectionController implements Initializable {

    @FXML
    private GridPane roomGrid;

    @FXML
    private Button btn_choose_room;

    @FXML
    private Button btn_back_to_registration;

    private Button selectedRoomButton = null;
    private int selectedRoomNumber = -1;
    private Connection connection;
    private DBHandler handler;
    private PreparedStatement pst;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = new DBHandler();
        createRoomGrid();
    }

    private void createRoomGrid() {
        try {
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
                                "-fx-background-color: #CC0000 ; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-weight: bold; -fx-font-size: 12px;");
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
            
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to all green if database query fails
            System.err.println("Error loading room data: " + e.getMessage());
            
            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load room status. Please try again later.");
            alert.showAndWait();
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
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Room Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a room before proceeding.");
            alert.showAndWait();
            return;
        }
        
        // Check if the selected room is already occupied
        try {
            connection = handler.connectDB();
            
            // Check if room is already occupied
            String checkQuery = "SELECT id FROM register_students WHERE room = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, selectedRoomNumber);
            java.sql.ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Room is already occupied
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Room Occupied");
                alert.setHeaderText(null);
                alert.setContentText("Room " + selectedRoomNumber + " is already occupied. Please select another room.");
                alert.showAndWait();
                return;
            }
            
            // Get the latest student ID without a room assigned
            String getLatestStudentQuery = "SELECT id FROM register_students WHERE room IS NULL ORDER BY id DESC LIMIT 1";
            PreparedStatement getStudentStmt = connection.prepareStatement(getLatestStudentQuery);
            rs = getStudentStmt.executeQuery();
            
            if (!rs.next()) {
                // No student found without a room
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No Student Found");
                alert.setHeaderText(null);
                alert.setContentText("No student found that needs a room assignment.");
                alert.showAndWait();
                return;
            }
            
            int studentId = rs.getInt("id");
            
            // Assign the room to the student
            String updateQuery = "UPDATE register_students SET room = ? WHERE id = ?";
            pst = connection.prepareStatement(updateQuery);
            pst.setInt(1, selectedRoomNumber);
            pst.setInt(2, studentId);
            int updated = pst.executeUpdate();
            
            if (updated > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Room Assigned");
                alert.setHeaderText(null);
                alert.setContentText("Room " + selectedRoomNumber + " has been assigned successfully!");
                alert.showAndWait();
                
                // Refresh the room grid to show the updated status
                roomGrid.getChildren().clear();
                createRoomGrid();
            }
            
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to assign room: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Navigate back to student management
        try {
            btn_choose_room.getScene().getWindow().hide();
            Stage studentStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Student/Manage_Student.fxml"));
            Scene scene = new Scene(root);
            studentStage.setScene(scene);
            studentStage.show();
            studentStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToRegistrationAction(MouseEvent event) {
        try {
            btn_back_to_registration.getScene().getWindow().hide();
            Stage registrationStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Student/New_Student.fxml"));
            Scene scene = new Scene(root);
            registrationStage.setScene(scene);
            registrationStage.show();
            registrationStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
