package com.example.javafx_school_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffAccount {

    @FXML
    private PasswordField passNew;

    @FXML
    private PasswordField passOld;

    @FXML
    private TextField txtNew;

    @FXML
    private TextField txtOld;

    @FXML
    void btnBack(ActionEvent event) throws IOException {
        HelloApplication.setRoot("StaffDashboard");
    }

    @FXML
    void btnRefresh(ActionEvent event) {
        txtOld.setText("");
        txtNew.setText("");
        passOld.setText("");
        passNew.setText("");
    }
    private boolean checkInput()
    {
        if(txtOld.getText().isBlank())
        {
            AlertOption.IncompleteInfo();
            return false;
        }
        if(txtNew.getText().isBlank())
        {
            AlertOption.IncompleteInfo();
            return false;
        }
        if(passOld.getText().isBlank())
        {
            AlertOption.IncompleteInfo();
            return false;
        }
        if(passNew.getText().isBlank())
        {
            AlertOption.IncompleteInfo();
            return false;
        }
        return true;
    }
    @FXML
    void btnSubmit(ActionEvent event) {
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }
        boolean check=checkInput();
        if(check==true){
            try {
                ResultSet r = connection.getUserInfo(connection.UserId);
                String oldName = txtOld.getText();
                String oldPass = passOld.getText();
                if (!oldName.equals(r.getString("username"))) {
                    AlertOption.incorrectInfo("Wrong username entered");
                    return;
                }

                if (!oldPass.equals(r.getString("password"))) {
                    AlertOption.incorrectInfo("Wrong password entered");
                    return;
                }
                String newName = txtNew.getText();
                String newPass = passNew.getText();
                connection.update_userLogin(connection.UserId, newName, newPass, "Active");
                AlertOption.MessageShow("Account updated! ");
            } catch (SQLException q) {
                System.out.println(q.getMessage());
            }}
    }

}
