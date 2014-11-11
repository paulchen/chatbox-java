package at.rueckgr.chatbox.dto;

import lombok.Data;

import java.io.Serializable;


@Data
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
}
