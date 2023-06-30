package com.example.javafx_school_management_system;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardManage implements Initializable {
      @FXML
    Label label_teacher_assigned;

    @FXML
    Label label_student_assigned;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     setLabel();
    }
    private void setLabel()
    {

        int teacher=connection.get_count_2("teacher_class_section");
        int student=connection.get_count_2("student_class_section");

        label_teacher_assigned.setText(String.valueOf(teacher));
        label_student_assigned.setText(String.valueOf(student));

    }


    @FXML
    void openAssignStudent() throws IOException {
       HelloApplication.setRoot("studentAssign");
    }

    @FXML
    void openAssignTeachers() throws IOException {
     HelloApplication.setRoot("ManageTeacher_ClassSection");
    }






}
