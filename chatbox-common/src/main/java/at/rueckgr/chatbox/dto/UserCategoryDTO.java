package at.rueckgr.chatbox.dto;

import java.io.Serializable;


public class UserCategoryDTO implements Serializable, ChatboxDTO {
    private static final long serialVersionUID = -6598512436583849295L;

    private final String name;
    private final String color;

    public UserCategoryDTO(String name, String color) {
        super();
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
