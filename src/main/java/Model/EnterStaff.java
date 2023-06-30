package Model;

public class EnterStaff {
    private Integer id;
    private String name;
    private Long Contact;
    private String Degree;
    private String status;
    private String Address;
    private String userName;
    private String userRole;

    public EnterStaff(Integer id, String name, Long contact, String degree, String status, String address, String userName, String userRole) {
        this.id = id;
        this.name = name;
        Contact = contact;
        Degree = degree;
        this.status = status;
        Address = address;
        this.userName = userName;
        this.userRole = userRole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getContact() {
        return Contact;
    }

    public void setContact(Long contact) {
        Contact = contact;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
