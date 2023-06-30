package com.example.javafx_school_management_system;

import Model.EnterClass;
import Model.EnterStaff;
import javafx.beans.binding.LongBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddStaff implements Initializable {

    @FXML
    private ToggleGroup Class;

    @FXML
    private TableView<EnterStaff> ClassTable;

    @FXML
    private TableColumn<EnterStaff, String> address_col;

    @FXML
    private TableColumn<EnterStaff,Integer> contact_col;

    @FXML
    private TableColumn<EnterStaff, String> degree_col;

    @FXML
    private TableColumn<EnterStaff, Integer> id_col;

    @FXML
    private TableColumn<EnterStaff, String> name_col;

    @FXML
    private RadioButton rdStaffActive;

    @FXML
    private RadioButton rdStaffAdmin;

    @FXML
    private RadioButton rdStaffInactive;

    @FXML
    private RadioButton rdStaffTeacher;

    @FXML
    private TableColumn<?, ?> role_col;

    @FXML
    private TextField staffAddress;

    @FXML
    private TextField staffContact;

    @FXML
    private TextField staffDegree;

    @FXML
    private TextField staffName;

    @FXML
    private TextField staffUser;

    @FXML
    private TableColumn<EnterStaff, String> status_col;

    @FXML
    private ToggleGroup userRole;
    @FXML
    private TextField FilterAddress;


    @FXML
    private TextField FilterContact;


    @FXML
    private TextField FilterDegree;

    @FXML
    private TextField FilterID;

    @FXML
    private TextField FilterName;

    @FXML
    private TextField FilterUserName;

    @FXML
    private ComboBox<String> cmbUserRole;
    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private TableColumn<EnterStaff , String> username_col;
    ObservableList<EnterStaff> StaffCollect = FXCollections.observableArrayList();
    ObservableList<String> status_1=FXCollections.observableArrayList("All","Active","Inactive");
    ObservableList<String> UserRole_1=FXCollections.observableArrayList("All","Admin","Teacher");
    ObservableList<EnterStaff> Filter = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        contact_col.setCellValueFactory(new PropertyValueFactory<>("contact"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("status"));
        degree_col.setCellValueFactory(new PropertyValueFactory<>("Degree"));
        username_col.setCellValueFactory(new PropertyValueFactory<>("userName"));
        role_col.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        address_col.setCellValueFactory(new PropertyValueFactory<>("Address"));
       loadList(null);
       cmbStatus.setItems(status_1);
       cmbStatus.getSelectionModel().select("All");
        cmbUserRole.setItems(UserRole_1);
        cmbUserRole.getSelectionModel().select("All");

    }
    private void loadList(String filter)  {
        try
        {
            if(filter==null)
            {
                if(!StaffCollect.isEmpty())
                {
                    StaffCollect.clear();
                }
                ResultSet rs = connection.get_staffs(null);
                while (rs.next())
                {
                    StaffCollect.add(new EnterStaff(rs.getInt("id"),rs.getString("name"), rs.getLong("contact"),rs.getString("degree"),rs.getString("status"),rs.getString("address"),rs.getString("user"),rs.getString("work")));
                   //ClassTable.setItems(StaffCollect);
                }
                ClassTable.setItems(StaffCollect);
            }
            else{
                if(!Filter.isEmpty())
                {
                    Filter.clear();
                }

                ResultSet rs = connection.get_staffs(filter);
                while (rs.next())
                {
                    Filter.add(new EnterStaff(rs.getInt("id"),rs.getString("name"), rs.getLong("contact"),rs.getString("degree"),rs.getString("status"),rs.getString("address"),rs.getString("user"),rs.getString("work")));
                }
                ClassTable.setItems(Filter);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void btnRefresh() {
        ClassTable.setItems(StaffCollect);
        resetForm();
    }

    @FXML
    private void btnFilter() {
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
        if(!FilterDegree.getText().equals("")){
            filter += " AND degree LIKE '%"+ FilterDegree.getText() +"%'";
        }
        if(!FilterUserName.getText().equals("")){
            filter += " AND user LIKE '%"+ FilterUserName.getText() +"%'";
        }

        if(!cmbStatus.getSelectionModel().getSelectedItem().equals("All")){
            String status =cmbStatus.getSelectionModel().getSelectedItem();
            //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
            filter += " AND status='"+ status +"'";
        }
        if(!cmbUserRole.getSelectionModel().getSelectedItem().equals("All")){
            String role =cmbUserRole.getSelectionModel().getSelectedItem();
            int work;
            if(role.equals("Admin"))
            {
                work=1;
            }
            else {
                work=2;
            }
            filter += " AND work ='"+ work +"'";
        }

        loadList(filter);
    }


    private int  checkDetails()
    {
        if((staffContact.getText().isBlank())||(staffUser.getText().isBlank())||(staffDegree.getText().isBlank())||(staffAddress.getText().isBlank())||(staffName.getText().isBlank()))
        {
            AlertOption.IncompleteInfo();
            return 1;
        }
        return 0;
    }


    private Boolean checkCorrectInput(String Input)
    {
        if(Input.equals("Contact"))
        {
            try {
                long a = Long.parseLong(staffContact.getText());
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
    protected void SubmitBTN()
    {

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
        saveRecord(CLASS_ID);
        if(CLASS_ID != null){
            AlertOption.MessageShow( "Record has been updated!");
        }else{
            AlertOption.MessageShow( "Record has been Inserted!");
        }
        resetForm();
    }
    private void saveRecord(String Staff_id)
    {
        StaffCollect.clear();
        String name=staffName.getText();
        Long contact=Long.parseLong(staffContact.getText());
        String Degree =staffDegree.getText();
        String user=staffUser.getText();
        String Address=staffAddress.getText();
        String status;
        if(rdStaffActive.isSelected())
        {
            status="Active";
        }
        else
        {
            status="Inactive";
        }
        int role;
        if(rdStaffAdmin.isSelected())
        {
            role=1;
        }
        else{
            role=2;
        }
        if(Staff_id != null){
            connection.update_staff(Integer.parseInt(Staff_id), name, role, status,Address,contact,Degree);
            loadList(null);

        }else{
            connection.insert_staff__(name,role,status,user,Address,contact,Degree);
            loadList(null);
        }
    }
    @FXML
    private void btnBack() throws IOException {
        AdminDashboard.Page=2;
        HelloApplication.setRoot("AdminDashboardHome");
    }
    private String CLASS_ID=null;

    public void resetForm(){
        //Filter.clear();
        FilterUserName.setText("");
        FilterDegree.setText("");
        FilterName.setText("");
        FilterContact.setText("");
        FilterID.setText("");
        FilterAddress.setText("");
        cmbUserRole.getSelectionModel().select("All");
        cmbStatus.getSelectionModel().select("All");
        staffName.setText("");
        staffAddress.setText("");
        staffContact.setText("");
        staffDegree.setText("");
        staffUser.setText("");
        Class.selectToggle(null);
//        txtFilterClass.setText("");
//        txtFilterID.setText("");
//        cmbFilterStatus.getSelectionModel().select("All");
          userRole.selectToggle(null);
        CLASS_ID = null;
    }

    @FXML
    private void btnDelete() {
         int ch=get_login_id.getId();
       if(ch!=ClassTable.getSelectionModel().getSelectedItem().getId()) {


           int i = ClassTable.getSelectionModel().getSelectedIndex();
           if (i >= 0) {
               int result = AlertOption.ConfirmBox("Sure? you want to delete.");
               if (result == 1) {
                   int ID = id_col.getCellData(i);
                   connection.delete_staff(ID);
                   // StaffCollect.remove(i);
               }
           } else {
               AlertOption.MessageShow("Please select the row to delete");
           }
       }
       else{
           AlertOption.incorrectInfo("it is an invalid request");
       }
        loadList(null);

        //ClassTable.setItems(newClass);
    }

    @FXML
    private void btnEdit() {
        int i = ClassTable.getSelectionModel().getSelectedIndex();
        if(i >=0 ) {

            String class_id = String.valueOf(id_col.getCellData(i));
            System.out.println("output: " + class_id);
            fillUser(class_id);
            CLASS_ID = class_id;

        }

    }
    public void fillUser(String class_id){
        try{
            ResultSet rs = connection.get_staff(Integer.parseInt(class_id));
            rs.next();

            staffUser.setText(rs.getString("user"));
            staffName.setText(rs.getString("name"));
            staffAddress.setText(rs.getString("address"));
            staffDegree.setText(rs.getString("degree"));
            staffContact.setText(String.valueOf(rs.getLong("contact")));
            if(rs.getString("status").equals("Active")){
                //  rActive.setSelected(true);
                rdStaffActive.selectedProperty().set(true);
            }else{
                // rInactive.setSelected(true);
                rdStaffInactive.selectedProperty().set(true);
            }
            if(rs.getInt("work")==1){
                //  rActive.setSelected(true);
                rdStaffAdmin.selectedProperty().set(true);
            }else{
                // rInactive.setSelected(true);
                rdStaffTeacher.selectedProperty().set(true);
            }
        } catch(SQLException e){
            System.err.println("Error : " + e.getMessage());
        }
    }

}
