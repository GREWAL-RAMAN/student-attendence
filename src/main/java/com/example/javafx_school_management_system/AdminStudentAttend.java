package com.example.javafx_school_management_system;

import Model.attend_student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminStudentAttend  implements Initializable {

    @FXML
    private TableView<attend_student> Attendance_table;

    @FXML
    private TableColumn<attend_student, Integer> col_ID;

    @FXML
    private TableColumn<attend_student, String> col_date;
   @FXML
   Button  SUBMIT_BTN;

    @FXML
    private TableColumn<attend_student, String> col_day;

    @FXML
    private TableColumn<attend_student, TextField> col_excuse;

    @FXML
    private TableColumn<attend_student, String> col_name;

    @FXML
    private TableColumn<attend_student, CheckBox> col_present;
    ObservableList<attend_student> student_atd_coll = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        col_ID.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_day.setCellValueFactory(new PropertyValueFactory<>("day"));
        col_present.setCellValueFactory(new PropertyValueFactory<>("present"));
        col_excuse.setCellValueFactory(new PropertyValueFactory<>("excuse"));
        loadList();
        SUBMIT_BTN.setDisable(false);

    }
    private void loadList()
    {
        try {

            {
                if(!student_atd_coll.isEmpty())
                {
                    student_atd_coll.clear();
                }
                LocalDate today=LocalDate.now();
                DateTimeFormatter a=DateTimeFormatter.ofPattern("yyyy-MM-dd");

                Format ad=new SimpleDateFormat("EEEE");
                String sds=ad.format(new Date());
                String sql="  WHERE status ='Active' ";
                ResultSet rs=connection.get_students(sql);
                while(rs.next())
                {
                    // System.out.println(rs.getString("student_id")+" "+rs.getString("student_class_section.student_name"));
                    if(!connection.checkStudentAttendance(rs.getInt("id"), today.format(a), "student_attendance")){
                        student_atd_coll.add(new attend_student(rs.getInt("id"),rs.getString("name")
                            ,today.format(a),sds.toLowerCase()));}
                }
                Attendance_table.setItems(student_atd_coll);
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());

        }
    }

    @FXML
    void btnBack() throws IOException {
        AdminDashboard.Page=4;
        HelloApplication.setRoot("AdminDashboardHome");
    }

    @FXML
    void btnSubmit() throws SQLException {
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }

        for(int i=0;i<student_atd_coll.size();i++)
        {

            String p= String.valueOf( student_atd_coll.get(i).getPresent().getSelectionModel().getSelectedItem());
            ResultSet rs=connection.get_staff_id(connection.UserId);
            int teach=rs.getInt("id");
                        connection.insert_student_attendance(teach,student_atd_coll.get(i).getStudent_id(),
                        student_atd_coll.get(i).getName(),student_atd_coll.get(i).getDate(),student_atd_coll.get(i).getDay(),p,
                        student_atd_coll.get(i).getExcuse().getText());
        }

        loadList();
        SUBMIT_BTN.setDisable(true);
    }

}
