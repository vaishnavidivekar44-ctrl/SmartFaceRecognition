package com.smartface.model;

import com.smartface.databas.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Attendance {
    private int empId;
    private String firstName;
    private String lastName;
    private String date;
    private String time;
    private String status;

    // Updated Constructor
    public Attendance(int empId, String firstName, String lastName, String date, String time, String status) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

 // In Attendance.java - logAttendance() method
    public void logAttendance() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO attendance (emp_id, first_name, last_name, attendance_date, attendance_time, status) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, this.empId);
            pstmt.setString(2, this.firstName);
            pstmt.setString(3, this.lastName);
            pstmt.setDate(4, java.sql.Date.valueOf(this.date));

            // Ensure time is in the correct format (HH:MM:SS)
            String validTime = this.time.contains(".") ? this.time.split("\\.")[0] : this.time;
            pstmt.setTime(5, java.sql.Time.valueOf(validTime));

            pstmt.setString(6, this.status);

            pstmt.executeUpdate();
            System.out.println("Attendance logged for Employee ID: " + this.empId);
        } catch (SQLException e) {
            System.out.println("Error logging attendance: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error in logging attendance: " + e.getMessage());
        }
    }



    // Getter methods (if needed)
    public int getEmpId() {
        return empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
