package com.example.javafx_school_management_system;

import Model.EnterStaff;
import Model.EnterStudent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddStudent implements Initializable {

    @FXML
    private ToggleGroup Class;

    @FXML
    private TableColumn<EnterStudent, String> Date_col1;

    @FXML
    private TextField FilterAddress;

    @FXML
    private TextField FilterContact;

    @FXML
    private TextField FilterGuardian;

    @FXML
    private TextField FilterID;

    @FXML
    private TextField FilterName;

    @FXML
    private TableColumn<EnterStudent,String> Guardian_col;

    @FXML
    private TableView<EnterStudent> StudentTable;

    @FXML
    private TableColumn<EnterStudent, String> address_col;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TableColumn<EnterStudent, Integer> contact_col;

    @FXML
    private TableColumn<EnterStudent, Integer> id_col;

    @FXML
    private TableColumn<EnterStudent, String> name_col;

    @FXML
    private RadioButton rdStaffActive;

    @FXML
    private RadioButton rdStaffInactive;

    @FXML
    private TableColumn<EnterStudent, String> status_col;

    @FXML
    private TextField studentContact;

    @FXML
    private DatePicker studentDate;

    @FXML
    private TextField studentGuardian;

    @FXML
    private TextField studentName;

    @FXML
    private TextField studentAddress;
    ObservableList<EnterStudent> studentCollect = FXCollections.observableArrayList();
    ObservableList<String> status_1=FXCollections.observableArrayList("All","Active","Inactive");
    ObservableList<EnterStudent> Filter = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        contact_col.setCellValueFactory(new PropertyValueFactory<>("contact"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("status"));
        Date_col1.setCellValueFactory(new PropertyValueFactory<>("date"));
        Guardian_col.setCellValueFactory(new PropertyValueFactory<>("guardian"));
        address_col.setCellValueFactory(new PropertyValueFactory<>("Address"));
         cmbStatus.setItems(status_1);
         cmbStatus.getSelectionModel().select("All");
         loadList(null);
    }
    private void loadList(String filter)  {
        try
        {
            if(filter==null)
            {
                if(!studentCollect.isEmpty())
                {
                    studentCollect.clear();
                }
                ResultSet rs = connection.get_students(null);
                while (rs.next())
                {
                    studentCollect.add(new EnterStudent(rs.getInt("id"),rs.getString("name"), rs.getString("date"),rs.getString("status"),rs.getString("guardian"),rs.getInt("contact"),rs.getString("address")));
                    //ClassTable.setItems(StaffCollect);
                }
                StudentTable.setItems(studentCollect);
            }
            else{
                if(!Filter.isEmpty())
                {
                    Filter.clear();
                }

                ResultSet rs = connection.get_students(filter);
                while (rs.next())
                {
                    Filter.add(new EnterStudent(rs.getInt("id"),rs.getString("name"), rs.getString("date"),rs.getString("status"),rs.getString("guardian"),rs.getInt("contact"),rs.getString("address")));
                }
                StudentTable.setItems(Filter);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private String STUDENT_ID=null;
    public void resetForm(){
        //Filter.clear();
        FilterName.setText("");
        FilterContact.setText("");
        FilterID.setText("");
        FilterAddress.setText("");
        cmbStatus.getSelectionModel().select("All");
        studentName.setText("");
        studentAddress.setText("");
        studentContact.setText("");
        studentGuardian.setText("");
        studentDate.getEditor().setText("");
        Class.selectToggle(null);
//        txtFilterClass.setText("");
//        txtFilterID.setText("");
//        cmbFilterStatus.getSelectionModel().select("All");
        STUDENT_ID = null;
    }


    private int  checkDetails()
    {
        if((studentContact.getText().isBlank())||(studentName.getText().isBlank())||(studentAddress.getText().isBlank())||(studentGuardian.getText().isBlank())||(studentDate.isShowing()))
        {
            AlertOption.IncompleteInfo();
            return 1;
        }
        return 0;
    }
    @FXML
    void SubmitBTN(ActionEvent event) {

        int g=checkDetails();
        if(g==1)
        {
            return;
        }
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }
        Boolean Check=checkCorrectInput("Contact");
        if(Check==false)
        {
            AlertOption.incorrectInfo("Please! Enter correct input order.");
            return;
        }
        saveRecord(STUDENT_ID);
        if(STUDENT_ID!= null){
            AlertOption.MessageShow( "Record has been updated!");
        }else{
            AlertOption.MessageShow( "Record has been Inserted!");
        }
        resetForm();

    }
    private void saveRecord(String Staff_id)
    {
        studentCollect.clear();
        String name=studentName.getText();
        Long contact=Long.parseLong(studentContact.getText());
        String Guardian =studentGuardian.getText();
        String Address=studentAddress.getText();
        String status;
        if(rdStaffActive.isSelected())
        {
            status="Active";
        }
        else
        {
            status="Inactive";
        }

        if(Staff_id != null){
            connection.update_student(Integer.parseInt(Staff_id), name,status,Address,contact,Guardian,studentDate.getEditor().getText());
            loadList(null);

        }else{
            connection.insert_student(name,status,Address,contact,studentDate.getEditor().getText(),Guardian);
            loadList(null);
        }
    }

    @FXML
    void btnBack(ActionEvent event) throws IOException {
        AdminDashboard.Page=2;
        HelloApplication.setRoot("AdminDashboardHome");
    }

    @FXML
    void btnDelete(ActionEvent event) {


        int i = StudentTable.getSelectionModel().getSelectedIndex();
        if(i>=0)
        {
            int result=  AlertOption.ConfirmBox("Sure? you want to delete.");
            if(result==1){
                int ID= id_col.getCellData(i);
                connection.delete_student(ID);
                // StaffCollect.remove(i);
            }
        }
        else{
            AlertOption.MessageShow("Please select the row to delete");
        }
        loadList(null);
        //ClassTable.setItems(newClass);
    }
    public void fillUser(String class_id){
        try{
            ResultSet rs = connection.get_student(Integer.parseInt(class_id));
            rs.next();

            studentDate.getEditor().setText(rs.getString("date"));
            studentName.setText(rs.getString("name"));
            studentAddress.setText(rs.getString("address"));
            studentGuardian.setText(rs.getString("guardian"));
            studentContact.setText(String.valueOf(rs.getLong("contact")));
            if(rs.getString("status").equals("Active")){
                //  rActive.setSelected(true);
                rdStaffActive.selectedProperty().set(true);
            }else{
                // rInactive.setSelected(true);
                rdStaffInactive.selectedProperty().set(true);
            }

        } catch(SQLException e){
            System.err.println("Error : " + e.getMessage());
        }
    }


    @FXML
    void btnEdit(ActionEvent event) {

        int i = StudentTable.getSelectionModel().getSelectedIndex();
        if(i >=0 ) {

            String id = String.valueOf(id_col.getCellData(i));
            System.out.println("output: " + id);
            fillUser(id);
            STUDENT_ID = id;

        }

    }

    private Boolean checkCorrectInput(String Input)
    {
        if(Input.equals("Contact"))
        {
            try {
                long a = Long.parseLong(studentContact.getText());
                return true;
            }catch (NumberFormatException e) {
                System.out.println("Input String cannot be parsed to Long.");
            }
            return false;
        }
        else if(Input.equals("ID"))
        {
            try {
                int a = Integer.parseInt(FilterID.getText());
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

        String filter = " WHERE 1=1  ";
        if(!FilterID.getText().equals("")){
            Boolean a=checkCorrectInput("ID");
            if(a==false)
            {
                AlertOption.incorrectInfo("Please! enter correct ID.");
                return;
            }
            filter += " AND id='"+ Integer.parseInt(FilterID.getText()) +"'";
        }
        if(!FilterContact.getText().equals("")){
            Boolean a=checkCorrectInput("Contact");
            if(a==false)
            {
                AlertOption.incorrectInfo("Please! enter correct Contact.");
                return;
            }
            filter += " AND contact='"+ Long.parseLong(FilterContact.getText()) +"'";
        }

        if(!FilterName.getText().equals("")){
            filter += " AND name LIKE '%"+ FilterName.getText() +"%'";
        }
        if(!FilterAddress.getText().equals("")){
            filter += " AND address LIKE '%"+ FilterAddress.getText() +"%'";
        }
        if(!FilterGuardian.getText().equals("")){
            filter += " AND guardian LIKE '%"+ FilterGuardian.getText() +"%'";
        }

        if(!cmbStatus.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmbStatus.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND status='"+ status +"'";
        }
        loadList(filter);
    }

    @FXML
    void btnRefresh(ActionEvent event) {
        StudentTable.setItems(studentCollect);
        resetForm();
    }

}
