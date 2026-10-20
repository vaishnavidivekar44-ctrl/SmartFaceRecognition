package com.smartface.databas;

import com.smartface.model.Attendance;
import com.smartface.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceLogger {
    public static void logAttendance(int empId, String status) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection is not available.");
            return;
        }

        // Fetch employee details using Employee model
        Employee employee = Employee.getEmployeeById(empId);
        if (employee == null) {
            System.out.println("Employee not found! Cannot log attendance.");
            return;
        }

        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        String date = LocalDate.now().toString();
        String time = LocalTime.now().toString();

        String query = "INSERT INTO attendance (emp_id, first_name, last_name, attendance_date, attendance_time, status) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, empId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, date);
            pstmt.setString(5, time);
            pstmt.setString(6, status);
            pstmt.executeUpdate();

            System.out.println("Attendance logged successfully for Employee: " + firstName + " " + lastName);

        } catch (SQLException e) {
            System.out.println("Error logging attendance: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Test logging attendance
        logAttendance(101, "Present"); // Adjust this test based on actual employee IDs
    }
}
