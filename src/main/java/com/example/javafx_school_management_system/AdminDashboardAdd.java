package com.example.javafx_school_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminDashboardAdd implements Initializable {


    @FXML
    Label label_class;
    @FXML
    Label label_student;
    @FXML
    Label label_staff;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    setLabel();

    }
    private void setLabel()
    {

            int classes =connection.get_count_2("manage_class");
            int student=connection.get_count_2("manage_student");
            int staff=connection.get_count_2("manage_staff");
            label_class.setText(String.valueOf(classes));
          label_staff.setText(String.valueOf(staff));
        label_student.setText(String.valueOf(student));



    }

    @FXML
    void openAddClass(ActionEvent event) throws IOException {
        HelloApplication.setRoot("AddClass");
    }


    @FXML
    void openAddStaff(ActionEvent event) throws IOException {
       HelloApplication.setRoot("AddStaff");
    }

    @FXML
    void openAddStudent(ActionEvent event) throws IOException {
  HelloApplication.setRoot("AddStudent");
    }

}
