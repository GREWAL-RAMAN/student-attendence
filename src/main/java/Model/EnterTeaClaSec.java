package Model;

public class EnterTeaClaSec {
    private int id;
    private String teacher_name;
    private int teacher_id;
    private int Class_id;
    private String class_name;

    private String status;

    public EnterTeaClaSec(int id, String teacher_name, int teacher_id, int class_id, String class_name, String status) {
        this.id = id;
        this.teacher_name = teacher_name;
        this.teacher_id = teacher_id;
        Class_id = class_id;
        this.class_name = class_name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getClass_id() {
        return Class_id;
    }

    public void setClass_id(int classSection_id) {
        Class_id = classSection_id;
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
