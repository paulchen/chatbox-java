package at.rueckgr.chatbox.dto;

import java.io.Serializable;


public class UserDTO implements Serializable {
    private static final long serialVersionUID = -2547473845270925254L;

    private final int id;
    private final String name;
    private final UserCategoryDTO userCategory;

    public UserDTO(int id, String name, UserCategoryDTO userCategory) {
        super();
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
}
