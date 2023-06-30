package Model;

public class EnterStudent {
     private int id;
     private String name;
     private String date;
     private String status;
     private String guardian;
     private int contact;
     private String address;

    public EnterStudent(int id, String name, String date, String status, String guardian, int contact, String address) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.guardian = guardian;
        this.contact = contact;
        this.address = address;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
