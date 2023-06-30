package com.example.javafx_school_management_system;

import Model.EnterStuTeaClaSec;
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

public class AssignStudents  implements Initializable {

    @FXML
    private ComboBox<String> cmbStudent;

    @FXML
    Button SUBMIT_btn;
    @FXML
    private ComboBox<String> cmbClass;

    @FXML
    private TableColumn<EnterStuTeaClaSec, String> column_Student_name;

    @FXML
    private TableColumn<EnterStuTeaClaSec, String> column_class_name;

    @FXML
    private TableColumn<EnterStuTeaClaSec, Integer> column_id;

    @FXML
    private TableColumn<EnterStuTeaClaSec, String> column_status;

    @FXML
    private TableColumn<EnterStuTeaClaSec, Integer> column_class_id;

    @FXML
    private TableColumn<EnterStuTeaClaSec, Integer> column_student_id;
    @FXML
    private ToggleGroup status;

    @FXML
    private RadioButton statusActive;


    @FXML
    Button EDIT_CANCEL;
    boolean edit_cancel=true;
    @FXML
    private RadioButton statusInactive;

    @FXML
    private TableView<EnterStuTeaClaSec> student_teacher_class_section_table;


    ObservableList<String> Student_collect= FXCollections.observableArrayList();
    ObservableList<Integer> Student_ID_collect= FXCollections.observableArrayList();

    ObservableList<String> Class_collect=FXCollections.observableArrayList();

    ObservableList<Integer> Class_ID=FXCollections.observableArrayList();
    ObservableList<EnterStuTeaClaSec>  StudentClassSection = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadComboBox();
       setTable();
       loadList();
       SUBMIT_btn.setDisable(true);

    }
    private void setTable()
    {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_student_id.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        column_Student_name.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        column_class_id.setCellValueFactory(new PropertyValueFactory<>("class_id"));
        column_class_name.setCellValueFactory(new PropertyValueFactory<>("class_name"));
        column_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void loadComboBox()  {
        try{
            if(!Student_collect.isEmpty())
            {
                Student_collect.clear();
            }
            if(!Student_ID_collect.isEmpty())
            {
                Student_ID_collect.clear();
            }
            if(!Class_collect.isEmpty())
            {
                Class_collect.clear();
            }
            if(!Class_collect.isEmpty())
            {
                Class_collect.clear();
            }

            String sql="EXCEPT SELECT student_id,student_name FROM student_class_section  ";
            ResultSet r=connection.get_table_for_combo("id","name","manage_student",sql);
            while(r.next())
            {
                Student_collect.add(r.getString("name"));
                Student_ID_collect.add(r.getInt("id"));
            }
            cmbStudent.setItems(Student_collect);

            sql=" WHERE status = 'Active' ";
            ResultSet s=connection.get_classes(sql);
            while(s.next()) {
                Class_ID.add(s.getInt("id"));
                Class_collect.add(s.getString("class_name"));
            }
            cmbClass.setItems(Class_collect);
        }
        catch (SQLException e)
        {
            e.getMessage();
            System.out.println("inside loadComboCox()");
        }
    }
    private void loadList()
    {
        try {
        if (!StudentClassSection.isEmpty()) {
            StudentClassSection.clear();
        }
        {
            ResultSet rs = connection.get_StudentClassesSections(null);
            while(rs.next())
            {
                StudentClassSection.add(new EnterStuTeaClaSec(rs.getInt("id"), rs.getInt("student_id"), rs.getString("student_name")
                        ,rs.getInt("class_id")
                        ,rs.getString("class_name"),rs.getString("status")));
            }
            student_teacher_class_section_table.setItems(StudentClassSection);
        }
    }catch (SQLException e)
    {
        System.out.println(e.getMessage());
    }
    }

    @FXML
   private  void set_submit()
    {
        if((cmbClass.getSelectionModel().getSelectedItem()!=null)&&(cmbStudent.getSelectionModel().getSelectedItem()!=null))
        {
          SUBMIT_btn.setDisable(false);
        }
        else{
            SUBMIT_btn.setDisable(true);
        }
    }
    @FXML
    void btnBack() throws IOException {
        AdminDashboard.Page=3;
        HelloApplication.setRoot("AdminDashboardHome");
    }

    @FXML
    void btnDelete() {
        int i = student_teacher_class_section_table.getSelectionModel().getSelectedIndex();
        if(i>=0)
        {
            int result=  AlertOption.ConfirmBox("Sure? you want to delete.");
            if(result==1){
                int ID= column_id.getCellData(i);
                connection.delete_StudentClassSection(ID);
                StudentClassSection.remove(i);

            }
        }
        else{
            AlertOption.MessageShow("Please select the row to delete");
        }
        student_teacher_class_section_table.setItems(StudentClassSection);
        loadComboBox();
    }
    private String STUDENT_TEACHER_CLASS_SECTION_ID=null;

    private void SaveData(String ID)
    {

        int index_1 = Student_ID_collect.get(cmbStudent.getSelectionModel().getSelectedIndex());
        String student_name=Student_collect.get(cmbStudent.getSelectionModel().getSelectedIndex());

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
            connection.insert_stu_cla_sec(index_1,index_2,student_name,class_name,status);
        }else
        {
            int index_3=column_id.getCellData(student_teacher_class_section_table.getSelectionModel().getSelectedIndex());
            connection.update_stu_cla_sec(index_3,index_1,student_name,index_2,class_name,status);
        }
        loadComboBox();
        cmbStudent.getSelectionModel().clearSelection();
        cmbClass.getSelectionModel().clearSelection();
        edit_cancel=true;
        EDIT_CANCEL.setText("Edit");
    }
    @FXML
    void btnEdit() {
        if(edit_cancel) {
            int i = student_teacher_class_section_table.getSelectionModel().getSelectedIndex();
            if (i >= 0)
            {
                Student_ID_collect.add(column_student_id.getCellData(i));
                Student_collect.add(column_Student_name.getCellData(i));
                cmbStudent.setItems(Student_collect);
                cmbStudent.getSelectionModel().select(column_Student_name.getCellData(i));
                if(Class_collect.contains(column_class_name.getCellData(i))) {
                    cmbClass.getSelectionModel().select(column_class_name.getCellData(i));
                }
                else{
                    Class_collect.add(column_class_name.getCellData(i));
                    Class_ID.add(column_class_id.getCellData(i));
                    cmbClass.getSelectionModel().select(column_class_name.getCellData(i));
                }
                if (column_status.getCellData(i).equals("Active")) {
                    statusActive.selectedProperty().set(true);
                } else {
                    statusInactive.selectedProperty().set(true);
                }
                STUDENT_TEACHER_CLASS_SECTION_ID = String.valueOf(column_id.getCellData(i));
                edit_cancel=false;
                EDIT_CANCEL.setText("cancel");

            }
            else{
                AlertOption.incorrectInfo("Please select a row to edit");
            }
        }
        else{

            edit_cancel=true;
            EDIT_CANCEL.setText("Edit");
            loadComboBox();
            cmbStudent.getSelectionModel().clearSelection();
            cmbClass.getSelectionModel().clearSelection();
        }
    }
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
        SaveData(STUDENT_TEACHER_CLASS_SECTION_ID);
        loadList();
        if(STUDENT_TEACHER_CLASS_SECTION_ID==null)
        {
            AlertOption.MessageShow("Field Has Been Inserted");
        }
        else {
            AlertOption.MessageShow("Field has been updated");
            STUDENT_TEACHER_CLASS_SECTION_ID=null;
        }


    }

}
