package com.smartface.model;

 class ModelTest {
    public static void main(String[] args) {
        // Testing Employee Class
        Employee emp1 = new Employee(101, "John", "Doe", "IT", "face_images/101_john.png");
        System.out.println("Employee Details:");
        System.out.println("ID: " + emp1.getEmpId());
        System.out.println("First Name: " + emp1.getFirstName());
        System.out.println("Last Name: " + emp1.getLastName());
        System.out.println("Department: " + emp1.getDepartment());
        System.out.println("Photo Path: " + emp1.getPhotoPath());

        // Testing Attendance Class with updated constructor
        Attendance attendance = new Attendance(emp1.getEmpId(), emp1.getFirstName(), emp1.getLastName(),
                                               "2025-03-25", "10:00 AM", "Present");

        System.out.println("\nAttendance Details:");
        System.out.println("Employee ID: " + attendance.getEmpId());
        System.out.println("First Name: " + attendance.getFirstName());
        System.out.println("Last Name: " + attendance.getLastName());
        System.out.println("Date: " + attendance.getDate());
        System.out.println("Time: " + attendance.getTime());
        System.out.println("Status: " + attendance.getStatus());

        // Test logAttendance() method
        attendance.logAttendance();
    }
}
