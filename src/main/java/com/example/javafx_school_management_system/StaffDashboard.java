package com.example.javafx_school_management_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffDashboard implements Initializable {
    @FXML
    BorderPane StaffBorderPane;
    @FXML
    AnchorPane StaffAnchorPane;
    @FXML
    PieChart PieChartStaff;
    @FXML
    Button btnSet;
    @FXML
    Button btnEdit;

    @FXML
    DatePicker dateEnd;
    @FXML
    DatePicker dateStart;
    private boolean editBtnStatus=true;
    @FXML
      private Label Label_text;
    ObservableList<PieChart.Data> PieData= FXCollections.observableArrayList();
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

    @FXML
    void btnAccount()  {
     loadPage("StaffAccount");
    }
    @FXML
    void btnHome()  {
      StaffBorderPane.setCenter(StaffAnchorPane);
    }
    @FXML
    void btnAttendance()
    {
        loadPage("staffAttendance");
    }

    @FXML
    void btnCheckAttendance()
    {
   loadPage("check_attendance_staff");
    }
    @FXML
    void btnSelfLeave()
    {
        loadPage("StaffSelfLeave");
    }

    @FXML
    void btnLogOut() throws IOException {
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }
        connection.closeConn();
     HelloApplication.setRoot("StaffLoginform");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateStart.setConverter(converter);
        dateEnd.setConverter(converter);
        getPresentAbsent();
        btnSet.setDisable(true);
        dateStart.setDisable(true);
        dateEnd.setDisable(true);
        LocalDate date= LocalDate.now();
        //Label_text.setText(date.toString());
        DateTimeFormatter a=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter b=DateTimeFormatter.ofPattern("EEEE");
        ResultSet sr=connection.get_staff_id(connection.UserId);
        boolean check=connection.check_holiday(date.format(a));
        if(check==true){
        try  {
                connection.insert_attendance(sr.getInt("id"),sr.getString("name"),date.format(a),date.format(b).toLowerCase(),"present","teachers_attendance");
             } catch (SQLException e)
             {
                 throw new RuntimeException(e);
             }
            }
        getPresentAbsent();
        btnSet.setDisable(true);
        dateStart.setDisable(true);
        dateEnd.setDisable(true);
        Label_text.setText(date.format(a));
        System.out.println(date);

    }
    @FXML
    protected void onClickRefresh()
    {
        getPresentAbsent();
    }
    protected void getPresentAbsent()  {
        if(!PieData.isEmpty())
        {
            PieData.clear();
        }
        ResultSet ff=connection.get_session(connection.UserId);
        try {
            String start_date;
            String end_date;
            if (ff.getString("date_start").equals("")||ff.getString("date_end").equals(""))
            {
                start_date="2020-01-01";
                end_date="2023-01-01";
            }
            else {
                start_date=ff.getString("date_start");
                end_date=ff.getString("date_end");
            }
            PieChartStaff.setTitle("User Id ("+connection.UserId+") Status");
            dateStart.setPromptText(start_date);
            dateEnd.setPromptText(end_date);
            int teacher_Id=ff.getInt("staff_ref");
            int Present_count=connection.get_count(teacher_Id,start_date,end_date,"present");
            PieData.add(new PieChart.Data("Present",Present_count));
            int Absent_count=connection.get_count(teacher_Id,start_date,end_date,"absent");
            PieData.add(new PieChart.Data("Absent",Absent_count));
            PieChartStaff.setData(PieData);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    protected void onClickEdit()
    {
        if(editBtnStatus==true)
        {
            btnEdit.setText("cancel");
            btnSet.setDisable(false);
            dateStart.setDisable(false);
            dateEnd.setDisable(false);
            editBtnStatus=false;
        }
        else {
            btnEdit.setText("Edit");
            btnSet.setDisable(true);
            dateStart.setDisable(true);
            dateEnd.setDisable(true);
            editBtnStatus=true;
        }
    }
    @FXML
    protected void onClickSet()
    {
        DateTimeFormatter a=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(dateStart.getValue()==null||dateEnd.getValue()==null)
        {
            return;
        }
        String start=dateStart.getValue().format(a);
        String End=dateEnd.getValue().format(a);
        int m=AlertOption.ConfirmBox("Are you sure");
        if(m==0)
        {
            return;

        }
        connection.update_session(start,End);
        btnEdit.setText("Edit");
        btnSet.setDisable(true);
        dateStart.setDisable(true);
        dateEnd.setDisable(true);
        editBtnStatus=true;
    }


    public  void   loadPage(String page)
    {
        Parent root=null;
        try{
            root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page + ".fxml")));

        }catch (IOException e)
        {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE,null,e);
            System.out.println(e.getMessage());
        }
        StaffBorderPane.setCenter(root);
    }

}
