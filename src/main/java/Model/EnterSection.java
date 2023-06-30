package Model;

public class EnterSection {
    private Integer id;
    private String name;
    private Integer order;
    private String status;

    public EnterSection(Integer id, String name, Integer order, String status) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.status = status;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
