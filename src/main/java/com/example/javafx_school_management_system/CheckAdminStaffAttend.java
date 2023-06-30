package com.example.javafx_school_management_system;

import Model.attend_staff;
import Model.attendace_stu_check;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CheckAdminStaffAttend implements Initializable {

    @FXML
    private ComboBox<String> cmbox_attendance;

    @FXML
    private ComboBox<String> cmbox_date_option;

    @FXML
    private ComboBox<String> cmbox_weekday;

    @FXML
    private TableColumn<attend_staff, String> col_date;

    @FXML
    private TableColumn<attend_staff, Integer> col_staff_id;

    @FXML
    private TableColumn<attend_staff, String> col_staff_name;

    @FXML
    private TableColumn<attend_staff, String> col_status;

    @FXML
    private TableColumn<attend_staff, String> col_week_day;

    @FXML
    private DatePicker date_ending;

    @FXML
    private DatePicker date_starting;

    @FXML
    private TableView<attend_staff> table_attendance;

    @FXML
    private TextField txt_teacher_name;
    @FXML
    private TextField txt_teacher_id;

    ObservableList<String> week_day_collect= FXCollections.observableArrayList("All","monday","tuesday","wednesday","thursday","friday","saturday");
    ObservableList<String> attendance_status_collect= FXCollections.observableArrayList("All","present","absent");
    ObservableList<String> date_option_collect= FXCollections.observableArrayList("day","specific date","starting-ending");

    ObservableList<attend_staff> Filter = FXCollections.observableArrayList();
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




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date_starting.setConverter(converter);
        date_ending.setConverter(converter);
     setComboBox();
     setTable();
        date_starting.setPromptText("");
        date_starting.setDisable(true);
        date_ending.setPromptText("");
        date_ending.setDisable(true);
    }

    public void setComboBox()
    {
     cmbox_attendance.setItems(attendance_status_collect);
     cmbox_attendance.getSelectionModel().select("All");

     cmbox_weekday.setItems(week_day_collect);
     cmbox_weekday.getSelectionModel().select("All");

     cmbox_date_option.setItems(date_option_collect);
     cmbox_date_option.getSelectionModel().select("day");
    }
    public void setTable()
    {
        col_staff_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_staff_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_week_day.setCellValueFactory(new PropertyValueFactory<>("day"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

    }
    private void loadList(String filter)  {
        try
      {
            if(!Filter.isEmpty())
            {
                Filter.clear();
            }

            ResultSet rs =connection.get_teacher_attend(filter);
            while (rs.next())
            {
                Filter.add(new attend_staff(rs.getInt("teachers_id"),rs.getString("teacher_name"),rs.getString("date_recorded"),rs.getString("weekday"),rs.getString("status")));
            }
            table_attendance.setItems(Filter);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void btnBack(ActionEvent event) throws IOException {
        AdminDashboard.Page=4;
    HelloApplication.setRoot("AdminDashboardHome");
    }

    private Boolean checkCorrectInput(String Input)
    {
        if(Input.equals("ID"))
        {
            try {
                int a = Integer.parseInt(txt_teacher_id.getText());
                return true;
            }catch (NumberFormatException e) {
                System.out.println("Input String cannot be parsed to Integer.");
            }
            return false;
        }
        return false;
    }

    @FXML
    void btnFilter(ActionEvent event) {
        String filter = " WHERE 1=1   ";
        if(!txt_teacher_id.getText().equals("")){
            Boolean a=checkCorrectInput("ID");
            if(a==false)
            {
                AlertOption.incorrectInfo("Please! enter correct ID.");
                return;
            }
            filter += " AND teachers_id='"+ Integer.parseInt(txt_teacher_id.getText()) +"'";
        }

        if(!txt_teacher_name.getText().equals("")){
            filter += " AND teacher_name LIKE '%"+ txt_teacher_name.getText() +"%'";
        }

        if(cmbox_date_option.getSelectionModel().getSelectedItem().equals("day"))
        { if(!cmbox_weekday.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmbox_weekday.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND weekday='"+ status +"'";
        }}
        else if(cmbox_date_option.getSelectionModel().getSelectedItem().equals("specific date"))
        {
            if(!date_starting.getEditor().getText().equals(""))
            {
                DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
                filter += " AND date_recorded ='"+ date_starting.getValue().format(a) +"'";

            }
        }
        else if(cmbox_date_option.getSelectionModel().getSelectedItem().equals("starting-ending"))
        {

            if((!date_starting.getEditor().getText().equals(""))||(!date_ending.getEditor().getText().equals("")))
            {
                DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
                filter += " AND date_recorded >='"+ date_starting.getValue().format(a) +"' AND date_recorded <= '"+
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

    @FXML
    void btnRefresh(ActionEvent event) {
        loadList(null);
        txt_teacher_name.setText("");
        txt_teacher_id.setText("");
        cmbox_weekday.getSelectionModel().select("All");
        cmbox_attendance.getSelectionModel().select("All");
        cmbox_date_option.getSelectionModel().select("day");
    }

    @FXML
    void comboDateSetting(ActionEvent event) {
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

}
