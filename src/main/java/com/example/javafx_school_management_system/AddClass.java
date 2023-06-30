package com.example.javafx_school_management_system;
import Model.*;

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
import java.util.*;

public class AddClass implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadList(null);
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        order_col.setCellValueFactory(new PropertyValueFactory<>("order"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("status"));
        ClassTable.setItems(newClass);
        cmbFilterStatus.setItems(status_1);
        cmbFilterStatus.getSelectionModel().select("All");
    }

    @FXML
    TableView<EnterClass> ClassTable;
    @FXML
    TableColumn<EnterClass, Integer> id_col;
    @FXML
    TableColumn<EnterClass, String> name_col;
    @FXML
    TableColumn<EnterClass, Integer> order_col;
    @FXML
    TableColumn<EnterClass, String> status_col;
    ObservableList<EnterClass> newClass = FXCollections.observableArrayList();
    ObservableList<EnterClass> Filter = FXCollections.observableArrayList();

    @FXML
    TextField txtClass;
    @FXML
    TextField txtOrder;
    @FXML
    ComboBox<String> cmbFilterStatus;
    @FXML
    TextField txtFilterClass;
    @FXML
    TextField txtFilterID;
    @FXML
    ToggleGroup Class;
    @FXML
    RadioButton rdActive;
    @FXML
    RadioButton rdInactive;

    ObservableList<String> status_1=FXCollections.observableArrayList("All","Active","Inactive");

    private void loadList(String filter)  {
        try
        {
            if(filter==null)
            {
            ResultSet rs = connection.get_classes(null);
            while (rs.next())
            {
                newClass.add(new EnterClass(rs.getInt("id"), rs.getString("class_name"), rs.getInt("sort_order"), rs.getString("status")));
            }}
            else{
                if(!Filter.isEmpty())
                {
                    Filter.clear();
                }

                ResultSet rs = connection.get_classes(filter);
                while (rs.next())
                {
                    Filter.add(new EnterClass(rs.getInt("id"), rs.getString("class_name"), rs.getInt("sort_order"), rs.getString("status")));
                }
                ClassTable.setItems(Filter);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private Boolean checkCorrectInput(String Input)
    {
        if(Input.equals("Order"))
        {
            try {
                int a = Integer.parseInt(txtOrder.getText());
                return true;
            }catch (NumberFormatException e) {
                System.out.println("Input String cannot be parsed to Integer.");
            }
            return false;
        }
        else if(Input.equals("ID"))
        {
            try {
                int a = Integer.parseInt(txtFilterID.getText());
                return true;
            }catch (NumberFormatException e) {
                System.out.println("Input String cannot be parsed to Integer.");
            }
            return false;
        }
        return false;
    }


    private void loadTable()  {
        ClassTable.setItems(newClass);
    }
    @FXML
    private void saveRecord(String class_id) throws SQLException {

        String class_name = txtClass.getText();
        int sort_order = Integer.parseInt(txtOrder.getText());
        String status;
        if(rdActive.isSelected())
        {
             status="Active";
        }
        else
        {
            status="Inactive";
        }
        if(class_id != null){
            connection.update_class(Integer.parseInt(class_id), class_name, sort_order, status);
           newClass.clear();
           loadList(null);

        }else{
          ResultSet  rs=  connection.insert_class(class_name, sort_order, status);
          newClass.add(new EnterClass(rs.getInt("id"), rs.getString("class_name"), rs.getInt("sort_order"), rs.getString("status")));
        }
        ClassTable.setItems(newClass);
    }
    private int  checkDetails()
    {
        if((txtClass.getText().isBlank())||(txtOrder.getText().isBlank()))
        {
            AlertOption.IncompleteInfo();
            return 1;
        }
        return 0;
    }


    @FXML
    private void btnSubmit() throws SQLException {

       int g=checkDetails();
        if(g==1)
        {
            return;
        }
        int o= AlertOption.ConfirmBox("Are you sure ? ");
        if(o!=1)
        {
            return;
        }
        Boolean Check=checkCorrectInput("Order");
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
    @FXML
    private void btnOnBack() throws IOException {
        AdminDashboard.Page=2;
        HelloApplication.setRoot("AdminDashboardHome");

    }
    private String CLASS_ID=null;

    public void resetForm(){
        Filter.clear();
        txtClass.setText("");
        txtOrder.setText("");
        txtFilterClass.setText("");
        txtFilterID.setText("");
        cmbFilterStatus.getSelectionModel().select("All");
        Class.selectToggle(null);
        CLASS_ID = null;
    }
     @FXML
    private void btnFilter() {
         String filter = " WHERE 1=1  ";
         if(!txtFilterID.getText().equals("")){
             Boolean a=checkCorrectInput("ID");
             if(a==false)
             {
                 AlertOption.incorrectInfo("Please! enter correct ID.");
                 return;
             }
             filter += " AND id='"+ Integer.parseInt(txtFilterID.getText()) +"'";
         }

         if(!txtFilterClass.getText().equals("")){
             filter += " AND class_name LIKE '%"+ txtFilterClass.getText() +"%'";
         }

         if(!cmbFilterStatus.getSelectionModel().getSelectedItem().equals("All")){
             String status =cmbFilterStatus.getSelectionModel().getSelectedItem();
             //((filterStatus.getSelectedItem().equals("Active"))?"1":"0");
             filter += " AND status='"+ status +"'";
         }

         loadList(filter);
    }


    @FXML
    private void btnRefresh() {
        ClassTable.setItems(newClass);
        resetForm();
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
            ResultSet rs = connection.get_class(Integer.parseInt(class_id));
            rs.next();

            txtClass.setText(rs.getString("class_name"));
            txtOrder.setText(rs.getString("sort_order"));
            if(rs.getString("status").equals("Active")){
                //  rActive.setSelected(true);
                rdActive.selectedProperty().set(true);
            }else{
                // rInactive.setSelected(true);
                rdInactive.selectedProperty().set(true);
            }
        } catch(SQLException e){
            System.err.println("Error : " + e.getMessage());
        }
    }

    @FXML
    private void btnDelete() {

      int i = ClassTable.getSelectionModel().getSelectedIndex();
      if(i>=0)
      {
          int result=  AlertOption.ConfirmBox("Sure? you want to delete.");
               if(result==1){
                  int ID= id_col.getCellData(i);
                  connection.delete_class(ID);

                  newClass.remove(i);
               }
      }
      else{
          AlertOption.MessageShow("Please select the row to delete");
      }
      loadTable();
      //ClassTable.setItems(newClass);
    }
}



