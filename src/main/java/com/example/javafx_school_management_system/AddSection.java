package com.example.javafx_school_management_system;

import Model.EnterClass;
import Model.EnterSection;
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

public class AddSection implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadList(null);
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        order_col.setCellValueFactory(new PropertyValueFactory<>("order"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("status"));
        SectionTable.setItems(newSection);
        cmbFilterStatus.setItems(status_1);
        cmbFilterStatus.getSelectionModel().select("All");
    }

    @FXML
    TableView<EnterSection> SectionTable;
    @FXML
    TableColumn<EnterSection, Integer> id_col;
    @FXML
    TableColumn<EnterSection, String> name_col;
    @FXML
    TableColumn<EnterSection, Integer> order_col;
    @FXML
    TableColumn<EnterSection, String> status_col;
    ObservableList<EnterSection> newSection = FXCollections.observableArrayList();
    ObservableList<EnterSection> Filter = FXCollections.observableArrayList();

    @FXML
    TextField txtSection;
    @FXML
    TextField txtOrder;
    @FXML
    ComboBox<String> cmbFilterStatus;
    @FXML
    TextField txtFilterSection;
    @FXML
    TextField txtFilterID;
    @FXML
    ToggleGroup Section;
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
                ResultSet rs = connection.get_Sections(null);
                while (rs.next())
                {
                    newSection.add(new EnterSection(rs.getInt("id"), rs.getString("name"), rs.getInt("sort_order"), rs.getString("status")));
                }}
            else{
                if(!Filter.isEmpty())
                {
                    Filter.clear();
                }

                ResultSet rs = connection.get_Sections(filter);
                while (rs.next())
                {
                    Filter.add(new EnterSection(rs.getInt("id"), rs.getString("name"), rs.getInt("sort_order"), rs.getString("status")));
                }
                SectionTable.setItems(Filter);

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
        SectionTable.setItems(newSection);
    }
    @FXML
    private void saveRecord(String section_id) throws SQLException {

        String name = txtSection.getText();

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
        if(section_id != null){
            connection.update_Section(Integer.parseInt(section_id), name, sort_order, status);
            newSection.clear();
            loadList(null);

        }else{
            ResultSet  rs=  connection.insert_Section(name, sort_order, status);
            newSection.add(new EnterSection(rs.getInt("id"), rs.getString("name"), rs.getInt("sort_order"), rs.getString("status")));
        }
        SectionTable.setItems(newSection);
    }
    private int  checkDetails()
    {
        if((txtSection.getText().isBlank())||(txtOrder.getText().isBlank()))
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
        int o=AlertOption.ConfirmBox("Are You sure !");
        if(o!=1)
        {
            return;
        }
        Boolean Check=checkCorrectInput("Order");
        if(Check==false)
        {
            AlertOption.incorrectInfo("Please! Enter correct input Order.");
            return;
        }
        saveRecord(SECTION_ID);
        if(SECTION_ID != null){
            AlertOption.MessageShow( "Record has been updated!");
        }else{
            AlertOption.MessageShow( "Record has been Inserted!");
        }
        resetForm();
    }
    @FXML
    private void btnBack() throws IOException {
        AdminDashboard.Page=2;
        HelloApplication.setRoot("AdminDashboardHome");
    }
    private String SECTION_ID=null;

    public void resetForm(){
        Filter.clear();
        txtSection.setText("");
        txtOrder.setText("");
        txtFilterSection.setText("");
        txtFilterID.setText("");
        cmbFilterStatus.getSelectionModel().select("All");
        Section.selectToggle(null);
        SECTION_ID = null;
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
            filter += " AND id='"+ Integer.valueOf(txtFilterID.getText()) +"'";
        }

        if(!txtFilterSection.getText().equals("")){
            filter += " AND name LIKE '%"+ txtFilterSection.getText() +"%'";
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
        SectionTable.setItems(newSection);
        resetForm();
    }

    @FXML
    private void btnEdit() {
        int i = SectionTable.getSelectionModel().getSelectedIndex();
        if(i >=0 ) {

            String section_id = String.valueOf(id_col.getCellData(i));
            System.out.println("output: " + section_id);
            fillUser(section_id);
            SECTION_ID = section_id;

        }

    }
    public void fillUser(String section_id){
        try{
            ResultSet rs = connection.get_Section(Integer.parseInt(section_id));
            rs.next();

            txtSection.setText(rs.getString("name"));
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

        int i = SectionTable.getSelectionModel().getSelectedIndex();
        if(i>=0)
        {
            int result=  AlertOption.ConfirmBox("Sure? you want to delete.");
            if(result==1){
                int ID= id_col.getCellData(i);
                connection.delete_Section(ID);
                newSection.remove(i);
            }
        }
        else{
            AlertOption.MessageShow("Please select the row to delete");
        }
        loadTable();
        //ClassTable.setItems(newClass);
    }

}
