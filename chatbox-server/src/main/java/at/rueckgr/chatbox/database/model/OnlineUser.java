package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the online_users database table.
 */
@Entity
@Table(name = "online_users")
@NamedQuery(name = "OnlineUser.findAll", query = "SELECT o FROM OnlineUser o")
public class OnlineUser implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @NotNull
    private Timestamp timestamp;

    //bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "user")
    private User userBean;

    public OnlineUser() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return new Timestamp(this.timestamp.getTime());
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = new Timestamp(timestamp.getTime());
    }

    public User getUserBean() {
        return this.userBean;
    }

    public void setUserBean(User userBean) {
        this.userBean = userBean;
    }

}