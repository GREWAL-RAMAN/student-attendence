package Model;

public class attend_staff {
    private int id;
    private String name;
    private String date;
    private String day;
    private String status;

    public attend_staff(int id, String name, String date, String day, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.day = day;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
