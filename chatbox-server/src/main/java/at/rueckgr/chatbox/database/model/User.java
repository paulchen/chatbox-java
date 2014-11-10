package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * The persistent class for the users database table.
 */
@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @NotNull
    private String name;

    //bi-directional many-to-one association to UserCategory
    @ManyToOne
    @JoinColumn(name = "category")
    private UserCategory userCategory;

    public User() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserCategory getUserCategory() {
        return this.userCategory;
    }

    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }

}