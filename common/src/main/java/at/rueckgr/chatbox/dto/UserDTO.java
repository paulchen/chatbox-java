package at.rueckgr.chatbox.dto;

import lombok.Data;

@Data
public class UserDTO implements ChatboxDTO {
    private static final long serialVersionUID = -2547473845270925254L;

    private int id;
    private String name;
    private UserCategoryDTO userCategory;

    public UserDTO() {
        super();
    }

    public UserDTO(int id, String name, UserCategoryDTO userCategory) {
        this.id = id;
        this.name = name;
        this.userCategory = userCategory;
    }
}
