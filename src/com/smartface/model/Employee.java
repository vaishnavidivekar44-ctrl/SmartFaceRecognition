package com.smartface.model;

import com.smartface.databas.DBConnection;
import com.smartface.camera.FaceTrainer;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int empId;
    private String firstName;
    private String lastName;
    private String department;
    private String photoPath;

    public Employee(int empId, String firstName, String lastName, String department, String photoPath) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.photoPath = photoPath;
    }

    // Getter methods
    public int getEmpId() {
        return empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    /**
     * Fetches employee details by ID.
     */
    public static Employee getEmployeeById(int empId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM employees WHERE emp_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String department = rs.getString("department");
                String photoPath = rs.getString("photo_path");

                return new Employee(empId, firstName, lastName, department, photoPath);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving employee: " + e.getMessage());
        }

        return null;
    }

    /**
     * Fetches all employees from the database.
     */
    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM employees";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int empId = rs.getInt("emp_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String department = rs.getString("department");
                String photoPath = rs.getString("photo_path");

                employees.add(new Employee(empId, firstName, lastName, department, photoPath));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Deletes an employee by ID and re-trains the face model.
     */
    public static void deleteEmployee(int empId) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM employees WHERE emp_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, empId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully with ID: " + empId);

                // Delete associated face images
                File faceDir = new File("face_images");
                File[] faceFiles = faceDir.listFiles((dir, name) -> name.startsWith(empId + "_"));

                if (faceFiles != null) {
                    for (File file : faceFiles) {
                        if (file.delete()) {
                            System.out.println("Deleted face image: " + file.getName());
                        } else {
                            System.out.println("Failed to delete face image: " + file.getName());
                        }
                    }
                }

                // Re-train face recognition model
                FaceTrainer trainer = new FaceTrainer();
                trainer.train();
                System.out.println("Face model retrained successfully after deletion.");
            } else {
                System.out.println("No employee found with ID: " + empId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
}
