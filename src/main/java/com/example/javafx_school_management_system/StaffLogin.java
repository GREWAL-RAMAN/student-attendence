package com.example.javafx_school_management_system;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffLogin implements Initializable {
    @FXML
    TextField staffTxtName;
    @FXML
    PasswordField staffPassword;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection.connection();
    }
    @FXML
    protected void checkLogin() throws IOException
    {
        //    connection.connection();
        String name=staffTxtName.getText();
        String pass=staffPassword.getText();
        int check=connection.checkUserLogin(name,pass);

        if (check != 0)
        {
            System.out.println("User ID = " + connection.UserId + " User Role: " + connection.User_Role);
            if (connection.User_Role == 2) {
                HelloApplication.setRoot("StaffDashboard");
            }
            else {
                AlertOption.incorrectInfo("Please! Enter the correct input.");
                staffTxtName.clear();
                staffPassword.clear();
            }
        }
          else {
              AlertOption.incorrectInfo("Please! Enter the correct input.");
            staffTxtName.clear();
            staffPassword.clear();
          }


    }
    @FXML
    private void btnBack() throws IOException {
        HelloApplication.setRoot("Login");
    }
    //@FXML
   // public void exitApplication(ActionEvent event) {
     //   Platform.exit();
   // }

}
