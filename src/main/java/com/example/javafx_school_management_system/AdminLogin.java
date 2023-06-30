package com.example.javafx_school_management_system;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminLogin implements Initializable {
   @FXML
    TextField adminTxtName;
    @FXML
    PasswordField adminPassword;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
   connection.connection();
    }



    @FXML

    protected void checkLogin() throws IOException
    {
    //    connection.connection();
        String name=adminTxtName.getText();
        String pass=adminPassword.getText();
        int check=connection.checkUserLogin(name,pass);

        if (check != 0) {
            System.out.println("User ID = " + connection.UserId + " User Role: "+ connection.User_Role);
            System.out.println("Correct login to dashboard " + check);
            get_login_id.setId(connection.UserId);
            HelloApplication.setRoot("AdminDashboardHome");
        }
        else {
         AlertOption.incorrectInfo("Please! Enter the correct input. ");

        }
    }

    @FXML
    private void onBack() throws IOException {
       HelloApplication.setRoot("Login");
    }
//    @FXML
//    public void exitApplication(ActionEvent event) {
//        Platform.exit();
//    }
}
