package com.smartface.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
    private static MainApp instance;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        instance = this;
        this.primaryStage = stage;
        showLoginScreen();
    }

    public static MainApp getInstance() {
        return instance;
    }

    public void showLoginScreen() {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("LoginScreen.fxml")), 800, 600);  // Set window size
            primaryStage.setTitle("Smart Face Recognition - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Error loading LoginScreen.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showDashboard() {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Dashboard.fxml")), 800, 600);  // Set window size
            primaryStage.setTitle("Smart Face Recognition - Dashboard");
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Error loading Dashboard.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showRegistrationScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartface/ui/RegistrationScreen.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);  // Set window size
            primaryStage.setTitle("Smart Face Recognition - Registration");
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Error loading Registration.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
