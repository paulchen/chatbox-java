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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDTO userDTO = (UserDTO) o;

        if (id != userDTO.id) {
            return false;
        }
        if (name != null ? !name.equals(userDTO.name) : userDTO.name != null) {
            return false;
        }
        if (userCategory != null ? !userCategory.equals(userDTO.userCategory) : userDTO.userCategory != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (userCategory != null ? userCategory.hashCode() : 0);
        return result;
    }
}
