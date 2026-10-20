package com.smartface.ui;

import com.smartface.databas.DBConnection;
import com.smartface.model.Employee;
import com.smartface.camera.FaceTrainer;  // Import FaceTrainer for training
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationScreen {
    @FXML
    private TextField empIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField departmentField;
    @FXML
    private Button registerButton;
    @FXML
    private Label statusLabel;

    private VideoCapture camera;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @FXML
    private void registerEmployee() {
        try {
            int empId = Integer.parseInt(empIdField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String department = departmentField.getText();
            String photoPath = "face_images/" + empId + "_" + firstName.toLowerCase() + ".png";

            // Capture face and save the image
            captureFace(photoPath);

            // Create an Employee object
            Employee employee = new Employee(empId, firstName, lastName, department, photoPath);

            // Save employee details to the database
            saveEmployeeToDatabase(employee);

            // Train the face recognizer after registration
            trainRecognizer();

            statusLabel.setText("Employee registered successfully and training completed!");

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid Employee ID! Please enter a valid number.");
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Error: Could not register employee.");
            System.out.println("Error registering employee: " + e.getMessage());
        }
    }

    private void captureFace(String photoPath) {
        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Error: Camera not accessible!");
            return;
        }

        Mat frame = new Mat();
        camera.read(frame);

        if (frame.empty()) {
            System.out.println("Error: No frame captured from the camera!");
            return;
        }

        // Convert to grayscale
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);

        // Ensure the directory exists
        File faceDir = new File("face_images");
        if (!faceDir.exists()) {
            faceDir.mkdirs();
        }

        Imgcodecs.imwrite(photoPath, frame);
        System.out.println("Face captured and saved at: " + photoPath);
        camera.release();
    }

    private void saveEmployeeToDatabase(Employee employee) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO employees (emp_id, first_name, last_name, department, photo_path) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, employee.getEmpId());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getLastName());
            pstmt.setString(4, employee.getDepartment());
            pstmt.setString(5, employee.getPhotoPath());

            pstmt.executeUpdate();
            System.out.println("Employee details saved to the database.");
        } catch (SQLException e) {
            System.out.println("Error saving to the database: " + e.getMessage());
        }
    }

    private void trainRecognizer() {
        try {
            FaceTrainer trainer = new FaceTrainer();
            trainer.train();
            System.out.println("Face recognizer trained successfully!");
        } catch (Exception e) {
            System.out.println("Error training the face recognizer: " + e.getMessage());
        }
    }
}
