package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


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

    //bi-directional many-to-one association to OnlineUser
    @OneToMany(mappedBy = "userBean")
    private List<OnlineUser> onlineUsers;

    //bi-directional many-to-one association to Shout
    @OneToMany(mappedBy = "user")
    private List<Shout> shouts;

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

    public List<OnlineUser> getOnlineUsers() {
        return this.onlineUsers;
    }

    public void setOnlineUsers(List<OnlineUser> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public OnlineUser addOnlineUser(OnlineUser onlineUser) {
        getOnlineUsers().add(onlineUser);
        onlineUser.setUserBean(this);

        return onlineUser;
    }

    public OnlineUser removeOnlineUser(OnlineUser onlineUser) {
        getOnlineUsers().remove(onlineUser);
        onlineUser.setUserBean(null);

        return onlineUser;
    }

    public List<Shout> getShouts() {
        return this.shouts;
    }

    public void setShouts(List<Shout> shouts) {
        this.shouts = shouts;
    }

    public Shout addShout(Shout shout) {
        getShouts().add(shout);
        shout.setUser(this);

        return shout;
    }

    public Shout removeShout(Shout shout) {
        getShouts().remove(shout);
        shout.setUser(null);

        return shout;
    }

    public UserCategory getUserCategory() {
        return this.userCategory;
    }

    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }

}