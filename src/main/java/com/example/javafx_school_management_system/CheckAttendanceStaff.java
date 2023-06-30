package com.example.javafx_school_management_system;


import Model.attendace_stu_check;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ResourceBundle;

public class CheckAttendanceStaff  implements Initializable {

    @FXML
    private ComboBox<String> cmbox_attendance;


    @FXML
    private ComboBox<String> cmbox_date_option;

    @FXML
    private ComboBox<String> cmbox_weekday;

    @FXML
    private TableColumn<attendace_stu_check,String> col_status;


    @FXML
    private TableColumn<attendace_stu_check, String> col_excuse;

    @FXML
    private TableColumn<attendace_stu_check, String> col_date;

    @FXML
    private TableColumn<attendace_stu_check, Integer> col_stu_id;

    @FXML
    private TableColumn<attendace_stu_check, String> col_stu_name;

    @FXML
    private TableColumn<attendace_stu_check, String> col_week_day;

    @FXML
    private DatePicker date_ending;

    @FXML
    private DatePicker date_starting;

    @FXML
    private TableView<attendace_stu_check> table_attendance;



    @FXML
    private TextField txt_student_id;

    @FXML
    private TextField txt_student_name;
     ObservableList<String> week_day_collect= FXCollections.observableArrayList("All","monday","tuesday","wednesday","thursday","friday","saturday");
    ObservableList<String> attendance_status_collect= FXCollections.observableArrayList("All","present","absent");
    ObservableList<String> date_option_collect= FXCollections.observableArrayList("day","specific date","starting-ending");

     int teacher_id_for;
    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    ObservableList<attendace_stu_check> Filter = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable();
        date_starting.setConverter(converter);
        date_ending.setConverter(converter);
       cmbox_weekday.setItems(week_day_collect);
       cmbox_weekday.getSelectionModel().select("All");
       cmbox_attendance.setItems(attendance_status_collect);
       cmbox_attendance.getSelectionModel().select("All");
       cmbox_date_option.setItems(date_option_collect);
       cmbox_date_option.getSelectionModel().select("day");
        date_starting.setPromptText("");
        date_starting.setDisable(true);
        date_ending.setPromptText("");
        date_ending.setDisable(true);
        ResultSet sr=connection.get_staff_id(connection.UserId);
        try {
            teacher_id_for=sr.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadList(null);
    }
    private void setTable()
    {
        col_stu_id.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        col_stu_name.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        col_week_day.setCellValueFactory(new PropertyValueFactory<>("day"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_excuse.setCellValueFactory(new PropertyValueFactory<>("excuse"));
    }
    @FXML
    private void comboDateSetting()
    {
     String get=   cmbox_date_option.getSelectionModel().getSelectedItem();
     if(get.equals("specific date"))
        {
            cmbox_weekday.setDisable(true);
            date_starting.setDisable(false);
            date_starting.setPromptText("date");
            date_ending.setPromptText("");
            date_ending.setDisable(true);
        }
     else if (get.equals("day"))
     {
         cmbox_weekday.getSelectionModel().select("All");
         cmbox_weekday.setDisable(false);
         date_starting.setPromptText("");
         date_starting.setDisable(true);
         date_ending.setPromptText("");
         date_ending.setDisable(true);
     }
     else
        {
            cmbox_weekday.setDisable(true);
            date_starting.setDisable(false);
            date_starting.setPromptText("starting date");
            date_ending.setDisable(false);
            date_ending.setPromptText("ending date");
        }
    }
    private void loadList(String filter)  {
        try
        {
               if(!Filter.isEmpty())
                {
                    Filter.clear();
                }

                ResultSet rs = connection.get_students_attend_2(teacher_id_for,filter);
                while (rs.next())
                {
                    Filter.add(new attendace_stu_check(rs.getInt("student_id"),rs.getString("student_name"),rs.getString("day"),rs.getString("date"),rs.getString("status"),rs.getString("excuse")));
                }
                table_attendance.setItems(Filter);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private Boolean checkCorrectInput(String Input)
    {
         if(Input.equals("ID"))
        {
            try {
                int a = Integer.parseInt(txt_student_id.getText());
                return true;
            }catch (NumberFormatException e) {
                System.out.println("Input String cannot be parsed to Integer.");
            }
            return false;
        }
        return false;
    }
    @FXML
    private void btnRefresh() {
        loadList(null);
        txt_student_name.setText("");
        txt_student_id.setText("");
        cmbox_weekday.getSelectionModel().select("All");
        cmbox_attendance.getSelectionModel().select("All");
        cmbox_date_option.getSelectionModel().select("day");
    }
    @FXML
    private void btnBack() throws IOException {
        HelloApplication.setRoot("StaffDashboard");
    }

    @FXML
    private void btnFilter() {
        String filter = "   ";
        if(!txt_student_id.getText().equals("")){
            Boolean a=checkCorrectInput("ID");
            if(a==false)
            {
                AlertOption.incorrectInfo("Please! enter correct ID.");
                return;
            }
            filter += " AND student_id='"+ Integer.parseInt(txt_student_id.getText()) +"'";
        }

        if(!txt_student_name.getText().equals("")){
            filter += " AND student_name LIKE '%"+ txt_student_name.getText() +"%'";
        }

       if(cmbox_date_option.getSelectionModel().getSelectedItem().equals("day"))
       { if(!cmbox_weekday.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmbox_weekday.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND day='"+ status +"'";
        }}
        else if(cmbox_date_option.getSelectionModel().getSelectedItem().equals("specific date"))
       {
           if(!date_starting.getEditor().getText().equals(""))
           {
               DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
               filter += " AND date ='"+ date_starting.getValue().format(a) +"'";

           }
       }
       else if(cmbox_date_option.getSelectionModel().getSelectedItem().equals("starting-ending"))
       {

           if((!date_starting.getEditor().getText().equals(""))||(!date_ending.getEditor().getText().equals("")))
           {
               DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
               filter += " AND date >='"+ date_starting.getValue().format(a) +"' AND date <= '"+
                       date_ending.getValue().format(a)+"'";
           }
       }
        if(!cmbox_attendance.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmbox_attendance.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND status ='"+ status +"'";
        }

        loadList(filter);
    }

}
