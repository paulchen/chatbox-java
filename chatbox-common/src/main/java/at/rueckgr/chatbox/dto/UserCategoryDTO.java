package at.rueckgr.chatbox.dto;

import java.io.Serializable;


public class UserCategoryDTO implements Serializable, ChatboxDTO {
    private static final long serialVersionUID = -6598512436583849295L;

    private String name;
    private String color;

    public UserCategoryDTO() {
        super();
    }

    public UserCategoryDTO(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
