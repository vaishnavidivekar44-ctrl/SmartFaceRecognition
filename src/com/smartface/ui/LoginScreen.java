package com.smartface.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginScreen {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple login check (can be enhanced for database verification)
        if (username.equals("admin") && password.equals("admin123")) {
            MainApp.getInstance().showDashboard();
        } else {
            System.out.println("Invalid credentials! Please try again.");
        }
    }

    @FXML
    private void showRegistration(ActionEvent event) {
        MainApp.getInstance().showRegistrationScreen();
    }
}
