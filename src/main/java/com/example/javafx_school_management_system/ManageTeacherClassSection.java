package com.example.javafx_school_management_system;

import Model.EnterClassSection;
import Model.EnterTeaClaSec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageTeacherClassSection implements Initializable {

    @FXML
    private TableView<EnterTeaClaSec> teacher_class_section_table;

    @FXML
    private ComboBox<String> cmbClass;

    @FXML
    private ComboBox<String> cmbTeacher;

    @FXML
    private TableColumn<EnterTeaClaSec, String> column_class_name;

    @FXML
    private TableColumn<EnterTeaClaSec,Integer> column_class_id;

    @FXML
    private TableColumn<EnterTeaClaSec, Integer> column_id;



    @FXML
    private TableColumn<EnterTeaClaSec, String> column_status;

    @FXML
    private TableColumn<EnterTeaClaSec, Integer> column_teacher_id;

    @FXML
    private TableColumn<EnterTeaClaSec, String> column_teacher_name;

    @FXML
    private ToggleGroup status;

    @FXML
    private RadioButton statusActive;

    @FXML
    private RadioButton statusInactive;
    @FXML
    Button SUBMIT_btn;
    @FXML
    Button EDIT_CANCEL;

    boolean edit_cancel=true;


    ObservableList<String> Teacher_collect= FXCollections.observableArrayList();
    ObservableList<Integer> Teacher_ID_collect= FXCollections.observableArrayList();


    ObservableList<String> Class_collect=FXCollections.observableArrayList();


    ObservableList<Integer> Class_ID=FXCollections.observableArrayList();
    ObservableList<EnterTeaClaSec>  TeacherClass = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBox();
        setTable();
        loadList();
        SUBMIT_btn.setDisable(true);
    }
    private void loadList()
    {  try {
        if (!TeacherClass.isEmpty()) {
            TeacherClass.clear();
        }
        { ResultSet rs = connection.get_TeacherClassesSections(null);
            while(rs.next())
            {
                TeacherClass.add(new EnterTeaClaSec(rs.getInt("id"),rs.getString("teacher_name")
                ,rs.getInt("teacher_id"),rs.getInt("class_id"),rs.getString("class_name")
                ,rs.getString("status")));
            }
            teacher_class_section_table.setItems(TeacherClass);
        }
    }catch (SQLException e)
    {
        System.out.println(e.getMessage());
    }
    }
    private void setTable()
    {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_teacher_name.setCellValueFactory(new PropertyValueFactory<>("teacher_name"));
        column_teacher_id.setCellValueFactory(new PropertyValueFactory<>("teacher_id"));
        column_class_id.setCellValueFactory(new PropertyValueFactory<>("Class_id"));
        column_class_name.setCellValueFactory(new PropertyValueFactory<>("class_name"));
        column_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void loadComboBox()  {
        try{
            if(!Teacher_collect.isEmpty())
            {
                Teacher_collect.clear();
            }
            if(!Teacher_ID_collect.isEmpty())
            {
                Teacher_ID_collect.clear();
            }
            if(!Class_ID.isEmpty())
            {
                Class_ID.clear();
            }
            if(!Class_collect.isEmpty())
            {
                Class_collect.clear();
            }
            String sql=" AND work ='2'  except SELECT teacher_id,teacher_name FROM teacher_class_section ";
            ResultSet r=connection.get_table_for_combo("id","name","manage_staff",sql);
            while(r.next())
            {
                Teacher_collect.add(r.getString("name"));
                Teacher_ID_collect.add(r.getInt("id"));
            }
            cmbTeacher.setItems(Teacher_collect);

             sql=" EXCEPT SELECT class_id , class_name  FROM teacher_class_section  ";
            ResultSet s=connection.get_table_for_combo("id","class_name","manage_class",sql);
            while(s.next()) {

                Class_ID.add(s.getInt("id"));
                Class_collect.add(s.getString("class_name"));
            }
            cmbClass.setItems(Class_collect);
        }
        catch (SQLException e)
        {
            System.out.println(  e.getMessage());
            System.out.println("inside loadComboCox()");
        }
    }
    @FXML
    void btnBack() throws IOException {
        AdminDashboard.Page=3;
        HelloApplication.setRoot("AdminDashboardHome");
    }

    @FXML
    void btnDelete() {
        int i = teacher_class_section_table.getSelectionModel().getSelectedIndex();
        if(i>=0)
        {
            int result=  AlertOption.ConfirmBox("Sure? you want to delete.");
            if(result==1){
                int ID= column_id.getCellData(i);
                connection.delete_TeacherClassSection(ID);
                TeacherClass.remove(i);
            }
        }
        else{
            AlertOption.MessageShow("Please select the row to delete");
        }
        loadComboBox();
        teacher_class_section_table.setItems(TeacherClass);
    }
    @FXML
    private  void set_submit()
    {
        if((cmbClass.getSelectionModel().getSelectedItem()!=null)&&(cmbTeacher.getSelectionModel().getSelectedItem()!=null))
        {
            SUBMIT_btn.setDisable(false);
        }
        else{
            SUBMIT_btn.setDisable(true);
        }
    }

    @FXML
    void btnEdit() {
        if(edit_cancel)
        {
            int i = teacher_class_section_table.getSelectionModel().getSelectedIndex();
            if (i >= 0)
            {
                Teacher_ID_collect.add(column_teacher_id.getCellData(i));
                Teacher_collect.add(column_teacher_name.getCellData(i));
                cmbTeacher.setItems(Teacher_collect);
                cmbTeacher.getSelectionModel().select(column_teacher_name.getCellData(i));

                Class_ID.add(column_class_id.getCellData(i));
                Class_collect.add(column_class_name.getCellData(i));
                cmbClass.setItems(Class_collect);
                cmbClass.getSelectionModel().select(column_class_name.getCellData(i));
                if (column_status.getCellData(i).equals("Active"))
                {
                    statusActive.selectedProperty().set(true);
                } else
                {
                    statusInactive.selectedProperty().set(true);
                }
                TEACHER_CLASS_SECTION_ID = String.valueOf(column_id.getCellData(i));
                edit_cancel=false;
                EDIT_CANCEL.setText("cancel");
            }
            else
            {
                AlertOption.incorrectInfo("Please select a row to edit");
            }
        }
        else
        {
            edit_cancel=true;
            EDIT_CANCEL.setText("Edit");
            loadComboBox();
            cmbClass.getSelectionModel().clearSelection();
            cmbClass.getSelectionModel().clearSelection();
        }
    }
    private void SaveData(String ID)
    {

        int index_1 = Teacher_ID_collect.get(Teacher_collect.indexOf(cmbTeacher.getSelectionModel().getSelectedItem()));
        String teacher_name=Teacher_collect.get(Teacher_collect.indexOf(cmbTeacher.getSelectionModel().getSelectedItem()));

        int index_2 = Class_ID.get(Class_collect.indexOf(cmbClass.getSelectionModel().getSelectedItem()));
        String class_name=Class_collect.get(Class_collect.indexOf(cmbClass.getSelectionModel().getSelectedItem()));


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
            connection.insert_TeacherClassSection(teacher_name,index_1,index_2,class_name,status);
        }else
        {
            int index_3=column_id.getCellData(teacher_class_section_table.getSelectionModel().getSelectedIndex());
            connection.update_TeacherClassSection(index_3,teacher_name,index_1,index_2,class_name,status);
        }
        loadComboBox();
        cmbTeacher.getSelectionModel().clearSelection();
        cmbClass.getSelectionModel().clearSelection();
        edit_cancel=true;
        EDIT_CANCEL.setText("Edit");
    }
    private String TEACHER_CLASS_SECTION_ID=null;
    private int  checkDetails()
    {

        return 0;
    }
    @FXML
    void btnSubmit() {
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
        SaveData(TEACHER_CLASS_SECTION_ID);
        loadList();
        if(TEACHER_CLASS_SECTION_ID==null)
        {
            AlertOption.MessageShow("Field Has Been Inserted");
        }
        else {
            AlertOption.MessageShow("Field has been updated");
            TEACHER_CLASS_SECTION_ID=null;
        }

    }
}
