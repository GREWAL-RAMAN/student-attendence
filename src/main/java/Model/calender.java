package Model;

public class calender {
    private  String date;
    private String day;
    private String holiday;
    private  String reason;

    public calender(String date, String day, String holiday, String reason) {
        this.date = date;
        this.day = day;
        this.holiday = holiday;
        this.reason = reason;
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

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
