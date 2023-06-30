package com.example.javafx_school_management_system;

import Model.attend_staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class StaffSelfLeave implements Initializable {

    @FXML
    private TableView<attend_staff> Self_leave_table;



    @FXML
    private ComboBox<String> cmbox_date_option;

    @FXML
    private ComboBox<String> cmbox_purpose;

    @FXML
    private ComboBox<String> cmbox_weekday;


    @FXML
    private TableColumn<attend_staff, String> date_col;

    @FXML
    private DatePicker date_ending;

    @FXML
    private DatePicker date_starting;

    @FXML
    private TableColumn<attend_staff, String> day_col;

    @FXML
    private Button submit_btn;

    @FXML
    private Button filter_btn;

    @FXML
    private Button delete_btn;


    @FXML
    private TableColumn<attend_staff, String> status_col;

    StringConverter<LocalDate> converter = new StringConverter<>() {
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
    ObservableList<attend_staff>  staff_filter= FXCollections.observableArrayList();
    ObservableList<String> week_day_collect= FXCollections.observableArrayList("All","monday","tuesday","wednesday","thursday","friday","saturday");
   ObservableList<String> date_option_collect= FXCollections.observableArrayList("day","specific date","starting-ending");
    ObservableList<String> edit_filter_collect= FXCollections.observableArrayList("Edit","Filter");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    date_starting.setConverter(converter);
    date_ending.setConverter(converter);
    setComboBox();
    setTable();
    loadList(null);
    submit_btn.setDisable(true);
    delete_btn.setDisable(true);
    date_ending.setDisable(true);
    date_starting.setDisable(true);
    }
    public void setComboBox()
    {


        cmbox_weekday.setItems(week_day_collect);
        cmbox_weekday.getSelectionModel().select("All");

        cmbox_date_option.setItems(date_option_collect);
        cmbox_date_option.getSelectionModel().select("day");

        cmbox_purpose.setItems(edit_filter_collect);
        cmbox_purpose.getSelectionModel().select("Filter");

    }
    public void setTable()
    {
        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        day_col.setCellValueFactory(new PropertyValueFactory<>("day"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void loadList(String filter)  {
        try
        {
            if(!staff_filter.isEmpty())
            {
                staff_filter.clear();
            }
            LocalDate date=LocalDate.now().plusDays(1);
            DateTimeFormatter a=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            ResultSet ss=connection.get_staff_id(connection.UserId);
            ResultSet rs =connection.get_teacher_attend_2(filter,ss.getInt("id"),date.format(a));
            while (rs.next())
            {
                staff_filter.add(new attend_staff(rs.getInt("teachers_id"),ss.getString("name"),rs.getString("date_recorded"),rs.getString("weekday"),rs.getString("status")));
            }
            Self_leave_table.setItems(staff_filter);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void btnSubmit() {
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }

        ResultSet ss=connection.get_staff_id(connection.UserId);
        DateTimeFormatter a=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter b=DateTimeFormatter.ofPattern("EEEE");
        try {
            connection.insert_attendance_2(ss.getInt("id"),ss.getString("name"),date_starting.getValue().format(a),date_starting.getValue().format(b).toLowerCase(),"absent","teachers_attendance");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadList(null);
    }

    @FXML
    void btnFilter() {
        String filter ="   ";
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


        loadList(filter);
    }

    @FXML
    void btnRefresh()  {
        loadList(null);
        cmbox_weekday.getSelectionModel().select("All");
        cmbox_date_option.getSelectionModel().select("day");

    }

    @FXML
    void btnDelete() {

      int i=  AlertOption.ConfirmBox("Are you sure!");
      if(i!=1)
      {
          return;
      }
      int m=Self_leave_table.getSelectionModel().getSelectedIndex();
     if(m>=0) {

            String date= date_col.getCellData(m);
            ResultSet ss = connection.get_staff_id(connection.UserId);

            try {
                connection.delete_attendance(ss.getInt("id"), date);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
     else{
         AlertOption.MessageShow("Select a row to delete");
     }

    loadList(null);
    }

    @FXML
    void comboDateSetting() {
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
    @FXML
    void dateStartingSetting() {
        LocalDate date= LocalDate.now().plusDays(1);
        DateTimeFormatter a=DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter b=DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter c=DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter d=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String Date_1=date_starting.getValue().format(a)+date_starting.getValue().format(b)+date_starting.getValue().format(c);
        String Date_2=date.format(a)+date.format(b)+date.format(c);
        if(Long.parseLong(Date_2)>Long.parseLong(Date_1))
        {
           AlertOption.incorrectInfo("Incorrect Date");
           date_starting.getEditor().setText(date.format(d));
        }
    }
    @FXML
    void EndDateSetting() {

        LocalDate date= LocalDate.now().plusDays(1);
        DateTimeFormatter a=DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter b=DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter c=DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter d=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String Date_1=date_ending.getValue().format(a)+date_ending.getValue().format(b)+date_ending.getValue().format(c);
        String Date_2=date.format(a)+date.format(b)+date.format(c);
        if(Long.parseLong(Date_2)>Long.parseLong(Date_1))
        {
            AlertOption.incorrectInfo("Incorrect Date");
            date_ending.getEditor().setText(date.format(d));
        }
    }
    @FXML
    void comboPurposeSetting() {
         String get=cmbox_purpose.getSelectionModel().getSelectedItem();
         if(get.equals("Edit"))
         {
             filter_btn.setDisable(true);
             delete_btn.setDisable(false);
             submit_btn.setDisable(false);
             cmbox_date_option.setDisable(true);
             date_starting.setDisable(false);
             cmbox_weekday.setDisable(true);
             date_ending.setDisable(true);
         }
         else {
             filter_btn.setDisable(false);
             delete_btn.setDisable(true);
             submit_btn.setDisable(true);
             cmbox_date_option.setDisable(false);
             cmbox_date_option.getSelectionModel().select("day");
             cmbox_weekday.setDisable(false);
             date_starting.setDisable(true);
             date_ending.setDisable(true);

         }
    }

}
