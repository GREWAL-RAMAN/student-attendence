package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class attend_student {
    private int student_id;
    private String name;
    private String day;
    private String date;
    private ComboBox<String> present;
    private TextField excuse;
    ObservableList<String> status_1= FXCollections.observableArrayList("Absent","Present","Half-day");


    public attend_student(int student_id, String name, String date,String day) {
        this.student_id = student_id;
        this.name = name;
        this.date = date;
        this.day=day;
        this.present=new ComboBox<>();
        this.present.setItems(status_1);
        this.present.getSelectionModel().selectFirst();
        this.excuse = new TextField();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ComboBox<String> getPresent() {
        return present;
    }

    public void setPresent(ObservableList<String> newStatus) {
        this.present.getEditor().clear();
        this.present.setItems(newStatus);
    }

    public TextField getExcuse() {
        return excuse;
    }

    public void setExcuse(TextField excuse) {
        this.excuse = excuse;
    }
}
