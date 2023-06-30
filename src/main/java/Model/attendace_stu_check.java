package Model;

public class attendace_stu_check {
    private int student_id;
    private String student_name;
    private String day;
    private String date;
    private String status;
    private String excuse;

    public attendace_stu_check(int student_id, String student_name, String day, String date, String status, String excuse) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.day = day;
        this.date = date;
        this.status = status;
        this.excuse = excuse;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExcuse() {
        return excuse;
    }

    public void setExcuse(String excuse) {
        this.excuse = excuse;
    }
}
