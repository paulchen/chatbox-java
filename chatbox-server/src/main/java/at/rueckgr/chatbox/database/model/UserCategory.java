package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the user_categories database table.
 */
@Entity
@Table(name = "user_categories")
@NamedQuery(name = "UserCategory.findAll", query = "SELECT u FROM UserCategory u")
public class UserCategory implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @NotNull
    private String color;

    @NotNull
    private String name;

    //bi-directional many-to-one association to User
    @OneToMany(mappedBy = "userCategory")
    private List<User> users;

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

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User addUser(User user) {
        getUsers().add(user);
        user.setUserCategory(this);

        return user;
    }

    public User removeUser(User user) {
        getUsers().remove(user);
        user.setUserCategory(null);

        return user;
    }

}