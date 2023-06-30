package com.example.javafx_school_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminAccount {

    @FXML
    private PasswordField passNew;

    @FXML
    private PasswordField passOld;

    @FXML
    private TextField txtNew;

    @FXML
    private TextField txtOld;

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
        boolean check=checkInput();
        if(check==false)
        {
            return;
        }
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }
        {
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
    @FXML
    private void btnBack() throws IOException {
        AdminDashboard.Page=5;
        HelloApplication.setRoot("AdminDashboardHome");
    }

    @FXML
    private void btnRefresh()  {
        txtOld.setText("");
        txtNew.setText("");
        passOld.setText("");
        passNew.setText("");
    }


}
