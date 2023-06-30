package com.example.javafx_school_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminDashboardAttendance {

    @FXML
    void btnStaff() throws IOException {
        HelloApplication.setRoot("check_admin_staff_attend");

    }

    @FXML
    void btnstudent(ActionEvent event) throws IOException {
          HelloApplication.setRoot("AdminCheck");
    }
    @FXML
    void btnAttendance(ActionEvent event) throws IOException {
        HelloApplication.setRoot("AdminStudentAttend");
    }


}
