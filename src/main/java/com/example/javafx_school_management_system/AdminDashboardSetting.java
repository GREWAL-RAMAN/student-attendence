package com.example.javafx_school_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminDashboardSetting {

    @FXML
    void BtnChangeAccount(ActionEvent event) throws IOException {
        HelloApplication.setRoot("AdminAccount");
    }

    @FXML
    void btnHolidaySetting(ActionEvent event) throws IOException {
       HelloApplication.setRoot("AdminHoliday");
    }

}
