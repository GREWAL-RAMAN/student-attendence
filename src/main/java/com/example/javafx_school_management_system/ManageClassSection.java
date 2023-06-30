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
import java.sql.RowId;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageClassSection implements Initializable {

    @FXML
    private TableView<EnterClassSection> class_section_table;

    @FXML
    private ComboBox<String> cmbClass;

    @FXML
    private ComboBox<String> cmbSection;

    @FXML
    private TableColumn<EnterClassSection,Integer> column_class_id;

    @FXML
    private TableColumn<EnterClassSection, String> column_class_name;
    @FXML
    private TableColumn<EnterClassSection, String> column_status;

    @FXML
    private ToggleGroup status;

    @FXML
    private RadioButton statusActive;

    @FXML
    private RadioButton statusInactive;
    @FXML
    private TableColumn<EnterClassSection, Integer> column_id;

    @FXML
    private TableColumn<EnterClassSection, Integer> column_section_id;

    @FXML
    private TableColumn<EnterClassSection, String> column_section_name;

    @FXML
       Button EDIT_BTN;
    boolean edit_cancel=true;
    ObservableList<String> class_collect= FXCollections.observableArrayList();
    ObservableList<Integer> class_ID_collect= FXCollections.observableArrayList();

    ObservableList<String> section_collect=FXCollections.observableArrayList();
    ObservableList<Integer> section_ID=FXCollections.observableArrayList();

    ObservableList<EnterClassSection> CLASS_SECTION = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadComboBox();
       setTable();
       loadList();
    }
    private void setTable()
    {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_class_name.setCellValueFactory(new PropertyValueFactory<>("class_name"));
        column_class_id.setCellValueFactory(new PropertyValueFactory<>("class_id"));
        column_section_name.setCellValueFactory(new PropertyValueFactory<>("section_name"));
        column_section_id.setCellValueFactory(new PropertyValueFactory<>("section_id"));
        column_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void loadList()  {
        try {
            if (!CLASS_SECTION.isEmpty()) {
            CLASS_SECTION.clear();
            }
            { ResultSet rs = connection.get_ClassesSections(null);
                   while(rs.next())
                   {
                       CLASS_SECTION.add(new EnterClassSection(
                               rs.getInt("id"), rs.getString("class_name"),
                               rs.getInt("class_ref"), rs.getString("section_name"),
                               rs.getInt("section_ref"), rs.getString("status")));
                   }
                   class_section_table.setItems(CLASS_SECTION);
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void loadComboBox()  {
       try{
           if(!class_collect.isEmpty())
           {
               class_collect.clear();
           }
           if(!class_ID_collect.isEmpty())
           {
               class_ID_collect.clear();
           }
           if(!section_collect.isEmpty())
           {
               section_collect.clear();
           }
           if(!section_ID.isEmpty())
           {
               section_ID.clear();
           }
//           String sql=" EXCEPT SELECT class_ref,class_name FROM class_section ";
//           ResultSet r=connection.get_table_for_combo("id","class_name","manage_class",sql);
           String sql="WHERE status ='Active' ";
           ResultSet r=connection.get_classes(sql);
         while(r.next())
         {
             class_collect.add(r.getString("class_name"));
             class_ID_collect.add(r.getInt("id"));
         }
         cmbClass.setItems(class_collect);
          sql=" WHERE status ='Active' ";
         ResultSet s=connection.get_Sections(sql);
         while(s.next()) {
             section_collect.add(s.getString("name"));
             section_ID.add(s.getInt("id"));
         }
         cmbSection.setItems(section_collect);
          }
       catch (SQLException e)
       {
           e.getMessage();
            System.out.println("inside loadComboCox()");
       }
     }
     @FXML

     private void SaveData(String ID)
     {

             int index_1 = class_ID_collect.get(cmbClass.getSelectionModel().getSelectedIndex());
             String class_name=class_collect.get(cmbClass.getSelectionModel().getSelectedIndex());
             int index_2 = section_ID.get(cmbSection.getSelectionModel().getSelectedIndex());
             String section_name=section_collect.get(cmbSection.getSelectionModel().getSelectedIndex());
             String status;
             if(statusActive.isSelected())
             {
                 status="Active";
             }
             else
             {
                 status="Inactive";
             }
         if(ID==null) {
             connection.insert_ClassSection(index_1, index_2, class_name, section_name, status);
         }else
         {
             int index_3=column_id.getCellData(class_section_table.getSelectionModel().getSelectedIndex());
             connection.update_ClassSection(index_3,class_name,index_1,section_name,index_2,status);
         }
         loadComboBox();
     }
     @FXML
     private void btnBack() throws IOException {
         AdminDashboard.Page=3;
         HelloApplication.setRoot("AdminDashboardHome");
     }
    private int  checkDetails() {
        if ((cmbSection.getSelectionModel().getSelectedItem()==null)|| (cmbClass.getSelectionModel().getSelectedItem()==null))
        {
            AlertOption.IncompleteInfo();
            return 1;
        }

        return 0;
    }
     @FXML
    private void btnSubmit()
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
         SaveData(CLASS_SECTION_ID);
         loadList();
         if(CLASS_SECTION_ID==null)
         {
         AlertOption.MessageShow("Field Has Been Inserted");
         }
         else {
             AlertOption.MessageShow("Field has been updated");
             CLASS_SECTION_ID=null;
         }
     }
    @FXML
    private void btnDelete() {

        int i = class_section_table.getSelectionModel().getSelectedIndex();
        if(i>=0)
        {
            int result=  AlertOption.ConfirmBox("Sure? you want to delete.");
            if(result==1){
                int ID= column_id.getCellData(i);
                connection.delete_ClassSection(ID);
                CLASS_SECTION.remove(i);
            }
        }
        else{
            AlertOption.MessageShow("Please select the row to delete");
        }
        class_section_table.setItems(CLASS_SECTION);
        loadComboBox();
        //ClassTable.setItems(newClass);
    }
    private String CLASS_SECTION_ID=null;

    public void resetForm(){
//        Filter.clear();
//        txtClass.setText("");
//        txtOrder.setText("");
//        txtFilterClass.setText("");
//        txtFilterID.setText("");
//        cmbFilterStatus.getSelectionModel().select("All");
//        Class.selectToggle(null);
//        CLASS_ID = null;
    }
    @FXML
    private void btnEdit() {
        if(edit_cancel) {
            EDIT_BTN.setText("cancel");
            int i = class_section_table.getSelectionModel().getSelectedIndex();
            if (i >= 0)
            {
               cmbClass.getSelectionModel().select(column_class_name.getCellData(i));
               cmbSection.getSelectionModel().select(column_section_name.getCellData(i));

                if (column_status.getCellData(i).equals("Active"))
                {
                    statusActive.selectedProperty().set(true);
                }
                else
                {
                    String sql="where id='"+column_section_id.getCellData(i)+"' ";
                    ResultSet rs=connection.get_Sections(sql);
                    sql="where id='"+column_class_id.getCellData(i)+"' ";
                     ResultSet ss= connection.get_classes(sql);
                     try{
                          if(rs.getString("status").equals("Inactive")||ss.getString("status").equals("Inactive"))
                          {
                               statusActive.setDisable(true);
                          }
                     }catch (SQLException e)
                     {
                         System.out.println(e.getMessage());
                     }
                    statusInactive.selectedProperty().set(true);
                }
                CLASS_SECTION_ID = String.valueOf(column_id.getCellData(i));

            }
           edit_cancel=false;
        }
        else {
            EDIT_BTN.setText("Edit");

            cmbClass.getSelectionModel().clearSelection();
            cmbSection.getSelectionModel().clearSelection();
            edit_cancel=true;
            CLASS_SECTION_ID=null;
        }

    }

}


