package com.smartface.camera;

import com.smartface.databas.DBConnection;

import com.smartface.model.Employee;
import com.smartface.model.Attendance;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import javax.swing.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;

public class FaceDetection {
    private VideoCapture camera;
    private CascadeClassifier faceDetector;
    private Set<Integer> sessionLoggedEmployees = new HashSet<>();  // Track recognized employees in current session
    private Set<Integer> recognizedEmployees = new HashSet<>();  // Ensure each employee is greeted only once

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void startFaceDetection() {
        camera = new VideoCapture(0);
        faceDetector = new CascadeClassifier("src/com/smartface/resources/haarcascade_frontalface_default.xml");

        if (!camera.isOpened() || faceDetector.empty()) {
            System.out.println("Error: Camera or Haarcascade not accessible.");
            return;
        }

        JFrame frame = new JFrame("Face Detection - Close Window to Stop");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setVisible(true);
        JLabel label = new JLabel();
        frame.add(label);

        Mat frameMat = new Mat();
        MatOfRect faces = new MatOfRect();
        boolean[] isRunning = {true};

        // Auto-stop after 15 seconds
        Timer timer = new Timer(15000, e -> {
            isRunning[0] = false;
            System.out.println("Face detection window closed after 15 seconds.");
        });
        timer.setRepeats(false);
        timer.start();

        while (isRunning[0] && camera.isOpened() && frame.isVisible()) {
            camera.read(frameMat);

            if (frameMat.empty()) {
                System.out.println("Error: No frame captured from the camera!");
                break;
            }

            detectFaces(frameMat, faces);

            for (Rect rect : faces.toArray()) {
                Imgproc.rectangle(frameMat, rect, new Scalar(0, 255, 0), 2);

                String tempFacePath = "face_images/temp_face.png";
                Imgcodecs.imwrite(tempFacePath, frameMat);

                int recognizedEmpId = recognizeFace(tempFacePath);

                if (recognizedEmpId != -1) {
                    handleRecognizedEmployee(recognizedEmpId);
                } else {
                    System.out.println("Unknown face detected. Please register!");
                }
            }

            label.setIcon(new ImageIcon(matToBufferedImage(frameMat)));
        }

        camera.release();
        frame.dispose();
        System.out.println("Face detection session ended.");
        sessionLoggedEmployees.clear();  // Clear session data for the next detection session
    }

    private void detectFaces(Mat frameMat, MatOfRect faces) {
        Imgproc.cvtColor(frameMat, frameMat, Imgproc.COLOR_BGR2GRAY);
        faceDetector.detectMultiScale(frameMat, faces, 1.1, 3, 0, new Size(30, 30), new Size());
    }

    private int recognizeFace(String facePath) {
        try {
            LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();
            recognizer.read("face_images/face_trainer.yml");

            org.bytedeco.opencv.opencv_core.Mat face = opencv_imgcodecs.imread(facePath, opencv_imgcodecs.IMREAD_GRAYSCALE);

            int[] label = new int[1];
            double[] confidence = new double[1];

            recognizer.predict(face, label, confidence);

            if (confidence[0] < 100) {
                System.out.println("Face recognized! ID: " + label[0] + ", Confidence: " + confidence[0]);
                return label[0];  // Return recognized employee ID
            }
        } catch (Exception e) {
            System.out.println("Error in recognizing face: " + e.getMessage());
        }

        return -1;  // If not recognized
    }

    private void handleRecognizedEmployee(int empId) {
        if (!sessionLoggedEmployees.contains(empId)) {
            Employee employee = Employee.getEmployeeById(empId);

            if (employee != null) {
                if (!recognizedEmployees.contains(empId)) {
                    System.out.println("Welcome, " + employee.getFirstName() + " " + employee.getLastName() + "!");
                    recognizedEmployees.add(empId);

                    // Show popup for attendance confirmation
                    Platform.runLater(() -> showAttendancePopup(employee));
                }

                String date = LocalDate.now().toString();
                String time = LocalTime.now().toString().split("\\.")[0];  // Ensures valid HH:MM:SS format
                String status = "Present";

                Attendance attendance = new Attendance(
                    empId,
                    employee.getFirstName(),
                    employee.getLastName(),
                    date,
                    time,
                    status
                );

                attendance.logAttendance();
                sessionLoggedEmployees.add(empId);
            } else {
                System.out.println("Employee not found in the database!");
            }
        }
    }

    // New method to show the attendance confirmation popup
    private void showAttendancePopup(Employee employee) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Attendance Logged");
        alert.setHeaderText("Welcome, " + employee.getFirstName() + " " + employee.getLastName() + "!");
        alert.setContentText("Your attendance has been successfully logged.");
        alert.showAndWait();
    }





    private BufferedImage matToBufferedImage(Mat mat) {
        int width = mat.width();
        int height = mat.height();
        byte[] data = new byte[width * height];
        mat.get(0, 0, data);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        image.getRaster().setDataElements(0, 0, width, height, data);
        return image;
    }
}
