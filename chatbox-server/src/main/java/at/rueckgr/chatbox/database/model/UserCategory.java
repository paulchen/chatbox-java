package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * The persistent class for the user_categories database table.
 */
@Entity
@Table(name = "user_categories")
@NamedQueries({
        @NamedQuery(name = UserCategory.FIND_ALL, query = "SELECT u FROM UserCategory u"),
        @NamedQuery(name = UserCategory.FIND_BY_NAME, query = "SELECT u FROM UserCategory u WHERE u.name = :name"),
})
public class UserCategory implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "UserCategory.findAll";
    public static final String FIND_BY_NAME = "UserCategory.findByName";

    @Id
    private Integer id;

    @NotNull
    private String color;

    @NotNull
    private String name;

    public UserCategory() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}