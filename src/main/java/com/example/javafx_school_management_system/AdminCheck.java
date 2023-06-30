package com.example.javafx_school_management_system;

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

public class AdminCheck implements Initializable {

    @FXML
    private ComboBox<String> cmb_date;

    @FXML
    private ComboBox<String> cmb_status;

    @FXML
    private ComboBox<String> cmb_week;

    @FXML
    private TableColumn<attendace_stu_check,String> col_date;

    @FXML
    private TableColumn<attendace_stu_check, String> col_day;

    @FXML
    private TableColumn<attendace_stu_check, String> col_excuse;

    @FXML
    private TableColumn<attendace_stu_check, Integer> col_id;

    @FXML
    private TableColumn<attendace_stu_check, String> col_name;

    @FXML
    private TableColumn<attendace_stu_check, String> col_status;

    @FXML
    private DatePicker date_end;

    @FXML
    private DatePicker date_start;

    @FXML
    private TableView<attendace_stu_check> student_table;

    @FXML
    private TextField txt_student_id;
    @FXML
    private TextField txt_student_name;


    ObservableList<String> week_day_collect= FXCollections.observableArrayList("All","monday","tuesday","wednesday","thursday","friday","saturday");
    ObservableList<String> attendance_status_collect= FXCollections.observableArrayList("All","present","absent");
    ObservableList<String> date_option_collect= FXCollections.observableArrayList("day","specific date","starting-ending");

    ObservableList<attendace_stu_check> Filter = FXCollections.observableArrayList();

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
        date_start.setConverter(converter);
        date_end.setConverter(converter);
        setComboBox();
        setTable();
        date_start.setPromptText("");
        date_start.setDisable(true);
        date_end.setPromptText("");
        date_end.setDisable(true);
        loadList(null);
    }
    public void setComboBox()
    {
        cmb_status.setItems(attendance_status_collect);
        cmb_status.getSelectionModel().select("All");

        cmb_week.setItems(week_day_collect);
        cmb_week.getSelectionModel().select("All");

        cmb_date.setItems(date_option_collect);
        cmb_date.getSelectionModel().select("day");

    }
    @FXML
    private void comboDateSetting()
    {
        String get=   cmb_date.getSelectionModel().getSelectedItem();
        if(get.equals("specific date"))
        {
            cmb_week.setDisable(true);
            date_start.setDisable(false);
            date_start.setPromptText("date");
            date_end.setPromptText("");
            date_end.setDisable(true);
        }
        else if (get.equals("day"))
        {
            cmb_week.getSelectionModel().select("All");
            cmb_week.setDisable(false);
            date_start.setPromptText("");
            date_start.setDisable(true);
            date_end.setPromptText("");
            date_end.setDisable(true);
        }
        else
        {
            cmb_week.setDisable(true);
            date_start.setDisable(false);
            date_start.setPromptText("starting date");
            date_end.setDisable(false);
            date_end.setPromptText("ending date");
        }
    }


    private void setTable()
    {
        col_id.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        col_day.setCellValueFactory(new PropertyValueFactory<>("day"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_excuse.setCellValueFactory(new PropertyValueFactory<>("excuse"));
    }
    private void loadList(String filter)  {
        try
        {
            if(!Filter.isEmpty())
            {
                Filter.clear();
            }

            ResultSet rs = connection.get_students_attend_3(filter);
            while (rs.next())
            {
                Filter.add(new attendace_stu_check(rs.getInt("student_id"),rs.getString("student_name"),rs.getString("day"),rs.getString("date"),rs.getString("status"),rs.getString("excuse")));
            }
            student_table.setItems(Filter);


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
    void btnBack() throws IOException {
        AdminDashboard.Page=4;
        HelloApplication.setRoot("AdminDashboardHome");
    }

    @FXML
    void btnFilter() {
        String filter = " WHERE 1=1   ";
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

        if(cmb_date.getSelectionModel().getSelectedItem().equals("day"))
        { if(!cmb_week.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmb_week.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND day='"+ status +"'";
        }}
        else if(cmb_date.getSelectionModel().getSelectedItem().equals("specific date"))
        {
            if(!date_start.getEditor().getText().equals(""))
            {
                DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
                filter += " AND date ='"+ date_start.getValue().format(a) +"'";

            }
        }
        else if(cmb_date.getSelectionModel().getSelectedItem().equals("starting-ending"))
        {

            if((!date_start.getEditor().getText().equals(""))||(!date_end.getEditor().getText().equals("")))
            {
                DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
                filter += " AND date >='"+ date_start.getValue().format(a) +"' AND date <= '"+
                        date_end.getValue().format(a)+"'";
            }
        }
        if(!cmb_status.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmb_status.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND status ='"+ status +"'";
        }

        loadList(filter);
    }

    @FXML
    void btnRefresh() {
        loadList(null);
        txt_student_name.setText("");
        txt_student_id.setText("");
        cmb_week.getSelectionModel().select("All");
        cmb_status.getSelectionModel().select("All");
        cmb_date.getSelectionModel().select("day");
    }

}
