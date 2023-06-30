package Model;

public class EnterClassSection {
    private Integer id;
    private String class_name;
    private Integer class_id;
    private String section_name;
    private Integer section_id;
    private String status;

    public EnterClassSection(Integer id, String class_name, Integer class_id, String section_name, Integer section_id,String status) {
        this.id = id;
        this.class_name = class_name;
        this.class_id = class_id;
        this.section_name = section_name;
        this.section_id = section_id;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public Integer getSection_id() {
        return section_id;
    }

    public void setSection_id(Integer section_id) {
        this.section_id = section_id;
    }
}
