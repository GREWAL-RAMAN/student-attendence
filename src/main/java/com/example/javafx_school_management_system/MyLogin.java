package com.example.javafx_school_management_system;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MyLogin {

    @FXML
    private TextField txtName;
    @FXML
    private PasswordField passLogin;

    @FXML
    protected void checkLogin() throws IOException
    {
        connection.connection();
        String name=txtName.getText();
        String pass=passLogin.getText();
        int check=connection.checkUserLogin(name,pass);


        if (check != 0) {
            System.out.println("User ID = " + connection.UserId + " User Role: ");
            System.out.println("Correct login to dashboard " + check);
            HelloApplication.setRoot("My-Calculator");
        }
        else {
            System.out.println("incorrect info entered");
        }
    }

}
