package Model;

public class EnterStuTeaClaSec {
    private int id;
    private int student_id;
    private String student_name;

    private int class_id;
    private String class_name;
    private String status;

    public EnterStuTeaClaSec(int id, int student_id, String student_name, int class_id, String class_name, String status) {
        this.id = id;
        this.student_id = student_id;
        this.student_name = student_name;
        this.class_id = class_id;
        this.class_name = class_name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
