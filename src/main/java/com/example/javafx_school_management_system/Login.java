package com.example.javafx_school_management_system;

import javafx.css.Declaration;
import javafx.css.Style;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


import java.io.IOException;

public class Login {

    @FXML
    protected void openAdminLogin() throws IOException {
        HelloApplication.setRoot("AdminLogin");
    }
    @FXML
    protected void openStaffLogin() throws IOException{
        HelloApplication.setRoot("StaffLoginForm");
    }


}
