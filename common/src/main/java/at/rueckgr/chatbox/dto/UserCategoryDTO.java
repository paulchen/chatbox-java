package at.rueckgr.chatbox.dto;

import lombok.Data;


@Data
public class UserCategoryDTO implements ChatboxDTO {
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
