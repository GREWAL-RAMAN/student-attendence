package com.example.javafx_school_management_system;

import Model.attendace_stu_check;
import Model.calender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminHoliday  implements Initializable {

    @FXML
    private ComboBox<String> cmBoc_holiday;

    @FXML
    private ComboBox<String> cmBox_date;

    @FXML
    private ComboBox<String> cmBox_purpose;

    @FXML
    private ComboBox<String> cmBox_weekday;

    @FXML
    private TableColumn<calender,String> col_date;

    @FXML
    private TableColumn<calender, String> col_day;

    @FXML
    private TableColumn<calender, String> col_holiday;

    @FXML
    private TableColumn<calender, String> col_holiday_name;

    @FXML
    private DatePicker date_ending;

    @FXML
    private DatePicker date_starting;

    @FXML
    private Button btn_submit;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_filter;

    @FXML
    private TableView<calender> table_calender;
    @FXML
    private TextField txt_reason;
    ObservableList<String> purpose_collect= FXCollections.observableArrayList("Edit","Filter");

    ObservableList<String> week_day_collect= FXCollections.observableArrayList("All","monday","tuesday","wednesday","thursday","friday","saturday","sunday");
    ObservableList<String> Holiday_collect= FXCollections.observableArrayList("All","yes","no");
    ObservableList<String> date_option_collect= FXCollections.observableArrayList("day","specific date","starting-ending");
    ObservableList<calender> Filter = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        date_starting.setConverter(converter);
        date_ending.setConverter(converter);
        setComboBox();
        setTable();
        loadList(null);
    }
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


    public void setComboBox()
    {
        cmBoc_holiday.setItems(Holiday_collect);
        cmBoc_holiday.getSelectionModel().select("All");

        cmBox_weekday.setItems(week_day_collect);
        cmBox_weekday.getSelectionModel().select("All");

        cmBox_date.setItems(date_option_collect);
        cmBox_date.getSelectionModel().select("day");

        cmBox_purpose.setItems(purpose_collect);
        cmBox_purpose.getSelectionModel().select("Filter");

        btn_edit.setDisable(true);
        btn_submit.setDisable(true);
        date_starting.setDisable(true);
        date_starting.setPromptText("");
        date_ending.setDisable(true);
        date_ending.setPromptText("");
    }
    public void setTable()
    {
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_day.setCellValueFactory(new PropertyValueFactory<>("day"));
        col_holiday.setCellValueFactory(new PropertyValueFactory<>("holiday"));
        col_holiday_name.setCellValueFactory(new PropertyValueFactory<>("reason"));
    }
    private void loadList(String filter)  {
        try
        {
            if(!Filter.isEmpty())
            {
                Filter.clear();
            }

            ResultSet rs =connection.get_calender(filter);
            while (rs.next())
            {
                Filter.add(new calender(rs.getString("d"),rs.getString("weekday"),rs.getString("holiday"),rs.getString("what_holiday")));
            }
            table_calender.setItems(Filter);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void comboPurposeSetting()
    {
        String get =cmBox_purpose.getSelectionModel().getSelectedItem();
        if(get.equals("Edit"))
        {
            btn_filter.setDisable(true);
            btn_edit.setDisable(false);

        }
        if(get.equals("Filter"))
        {
            btn_filter.setDisable(false);
            btn_edit.setDisable(true);
            btn_submit.setDisable(true);

        }

    }
    @FXML
    private void comboDateSetting()
    {
        String get=   cmBox_date.getSelectionModel().getSelectedItem();
        if(get.equals("specific date"))
        {
            cmBox_weekday.setDisable(true);
            date_starting.setDisable(false);
            date_starting.setPromptText("date");
            date_ending.setPromptText("");
            date_ending.setDisable(true);
        }
        else if (get.equals("day"))
        {
            cmBox_weekday.getSelectionModel().select("All");
            cmBox_weekday.setDisable(false);
            date_starting.setDisable(true);
            date_ending.setPromptText("");
            date_ending.setDisable(true);
        }
        else
        {
            cmBox_weekday.setDisable(false);
            date_starting.setDisable(false);
            date_starting.setPromptText("starting date");
            date_ending.setDisable(false);
            date_ending.setPromptText("ending date");
        }
    }

    @FXML
    void btnBack(ActionEvent event) throws IOException {
        AdminDashboard.Page=5;
        HelloApplication.setRoot("AdminDashboardHome");
    }

    @FXML
    void btnEdit(ActionEvent event) {
        if(edit==false)
        {

            int i = table_calender.getSelectionModel().getSelectedIndex();
            if (i >= 0) {

               date_starting.setDisable(false);
                String date = col_date.getCellData(i);
                txt_reason.setText(col_holiday_name.getCellData(i));
                cmBox_weekday.getSelectionModel().select(col_day.getCellData(i));
                cmBoc_holiday.getSelectionModel().select(col_holiday.getCellData(i));
                date_starting.getEditor().setText(col_date.getCellData(i));
                edit = true;
                btn_edit.setText("cancel");
                btn_submit.setDisable(false);
            }
        }
        else {
            edit=false;
             btn_submit.setDisable(true);
             btn_edit.setText("Edit");
             table_calender.refresh();
        }
    }
    private boolean edit=false;



    @FXML
    void btnSubmit() {
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }
     String holiday=  cmBoc_holiday.getSelectionModel().getSelectedItem();
     String event= txt_reason.getText();

        String date1=col_date.getCellData(table_calender.getSelectionModel().getSelectedIndex());

      int i=  AlertOption.ConfirmBox("Are you sure?");
      if(i==1)
      {

          connection.update_calendar(date1, holiday, event);

      }
      loadList(null);
      btn_submit.setDisable(true);
      btn_edit.setText("Edit");
      edit=false;
      }

    @FXML
    void btnFilter() {
        String filter = " WHERE 1=1  ";
        if(!txt_reason.getText().equals("")){
            filter += " AND what_holiday  LIKE '%"+ txt_reason.getText() +"%'";
        }

        if(cmBox_date.getSelectionModel().getSelectedItem().equals("day"))
        {
            if(!cmBox_weekday.getSelectionModel().getSelectedItem().equals("All"))
            {
            String status =cmBox_weekday.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND weekday='"+ status +"'";
            }
        }
        else if(cmBox_date.getSelectionModel().getSelectedItem().equals("specific date"))
        {
            if(!date_starting.getEditor().getText().equals(""))
            {
                DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
                filter += " AND d ='"+ date_starting.getValue().format(a) +"'";

            }
        }
        else if(cmBox_date.getSelectionModel().getSelectedItem().equals("starting-ending"))
        {
            if(!cmBox_weekday.getSelectionModel().getSelectedItem().equals("All"))
            {
                String status =cmBox_weekday.getSelectionModel().getSelectedItem();
                //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
                filter += " AND weekday='"+ status +"'";
            }

            if((!date_starting.getEditor().getText().equals(""))||(!date_ending.getEditor().getText().equals("")))
            {
                DateTimeFormatter a =DateTimeFormatter.ofPattern("yyyy-MM-dd");
                filter += " AND d >='"+ date_starting.getValue().format(a) +"' AND d <= '"+
                        date_ending.getValue().format(a)+"'";
            }
        }
        if(!cmBoc_holiday.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmBoc_holiday.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND holiday ='"+ status +"'";
        }
        loadList(filter);
    }

    @FXML
    void btnRefresh(ActionEvent event) {
      txt_reason.setText("");
      date_starting.setPromptText("");
      date_ending.setPromptText("");
      cmBoc_holiday.getSelectionModel().select("All");
      cmBox_weekday.getSelectionModel().select("All");
      loadList(null);
    }

}
