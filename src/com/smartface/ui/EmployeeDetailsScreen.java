package com.smartface.ui;

import com.smartface.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.List;

public class EmployeeDetailsScreen {
    @FXML
    private TextField empIdField;
    @FXML
    private TextArea detailsArea;

    @FXML
    private void fetchEmployeeDetails() {
        String empIdText = empIdField.getText();
        try {
            int empId = Integer.parseInt(empIdText);
            Employee employee = Employee.getEmployeeById(empId);

            if (employee != null) {
                String details = String.format("ID: %d\nFirst Name: %s\nLast Name: %s\nDepartment: %s\nPhoto Path: %s",
                        employee.getEmpId(), employee.getFirstName(), employee.getLastName(), 
                        employee.getDepartment(), employee.getPhotoPath());
                detailsArea.setText(details);
            } else {
                detailsArea.setText("No employee found with the given ID.");
            }
        } catch (NumberFormatException e) {
            detailsArea.setText("Invalid Employee ID. Please enter a valid number.");
        }
    }

    @FXML
    private void fetchAllEmployees() {
        List<Employee> allEmployees = Employee.getAllEmployees();

        if (allEmployees.isEmpty()) {
            detailsArea.setText("No employees found.");
        } else {
            StringBuilder details = new StringBuilder("All Employees:\n\n");
            for (Employee emp : allEmployees) {
                details.append(String.format("ID: %d | Name: %s %s | Department: %s\n",
                        emp.getEmpId(), emp.getFirstName(), emp.getLastName(), emp.getDepartment()));
            }
            detailsArea.setText(details.toString());
        }
    }

    @FXML
    private void closeScreen() {
        Stage stage = (Stage) empIdField.getScene().getWindow();
        stage.close();
    }
}
