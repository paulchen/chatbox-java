package at.rueckgr.chatbox.dto;

import java.io.Serializable;


public class UserDTO implements Serializable, ChatboxDTO {
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserCategoryDTO getUserCategory() {
        return userCategory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserCategory(UserCategoryDTO userCategory) {
        this.userCategory = userCategory;
    }
}
