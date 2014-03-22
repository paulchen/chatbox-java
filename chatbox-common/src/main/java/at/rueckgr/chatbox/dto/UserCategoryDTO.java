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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserCategoryDTO that = (UserCategoryDTO) o;

        if (color != null ? !color.equals(that.color) : that.color != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
