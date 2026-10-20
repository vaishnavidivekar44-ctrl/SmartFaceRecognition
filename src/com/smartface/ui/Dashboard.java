package com.smartface.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.smartface.camera.FaceDetection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

public class Dashboard {
    @FXML
    private Button logoutButton;

    private FaceDetection faceDetection;

    public Dashboard() {
        faceDetection = new FaceDetection();
    }

    @FXML
    private void startFaceDetection(ActionEvent event) {
        System.out.println("Starting face detection...");
        faceDetection.startFaceDetection();
    }

    @FXML
    private void viewAttendance(ActionEvent event) {
        System.out.println("Viewing attendance...");
        try {
            // Load AttendanceScreen.fxml and display it
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartface/ui/AttendanceScreen.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Attendance Records");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Attendance Screen: " + e.getMessage());
        }
    }

    @FXML
    private void viewEmployeeDetails(ActionEvent event) {
        System.out.println("Viewing employee details...");
        try {
            // Load EmployeeDetailsScreen.fxml and display it
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartface/ui/EmployeeDetailsScreen.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Employee Details");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Employee Details Screen: " + e.getMessage());
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        System.out.println("Logging out...");
        MainApp.getInstance().showLoginScreen();
    }
}
