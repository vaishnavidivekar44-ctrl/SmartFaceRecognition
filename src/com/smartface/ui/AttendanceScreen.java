package com.smartface.ui;

import com.smartface.databas.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.smartface.model.Attendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendanceScreen {
    @FXML
    private TableView<Attendance> attendanceTable;
    @FXML
    private TableColumn<Attendance, Integer> empIdColumn;
    @FXML
    private TableColumn<Attendance, String> firstNameColumn;
    @FXML
    private TableColumn<Attendance, String> lastNameColumn;
    @FXML
    private TableColumn<Attendance, String> dateColumn;
    @FXML
    private TableColumn<Attendance, String> timeColumn;
    @FXML
    private TableColumn<Attendance, String> statusColumn;

    private ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        empIdColumn.setCellValueFactory(new PropertyValueFactory<>("empId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadAttendanceData();
    }

    private void loadAttendanceData() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT emp_id, first_name, last_name, attendance_date, attendance_time, status FROM attendance";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int empId = rs.getInt("emp_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String date = rs.getString("attendance_date");
                String time = rs.getString("attendance_time");
                String status = rs.getString("status");

                attendanceList.add(new Attendance(empId, firstName, lastName, date, time, status));
            }

            attendanceTable.setItems(attendanceList);

        } catch (SQLException e) {
            System.out.println("Error loading attendance data: " + e.getMessage());
        }
    }
}
