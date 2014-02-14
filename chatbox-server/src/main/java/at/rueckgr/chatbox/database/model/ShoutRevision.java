package at.rueckgr.chatbox.database.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the shout_revisions database table.
 */
@Entity
@Table(name = "shout_revisions")
@NamedQuery(name = "ShoutRevision.findAll", query = "SELECT s FROM ShoutRevision s")
public class ShoutRevision implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ShoutRevisionPK id;

    @NotNull
    private Timestamp date;

    @NotNull
    private Timestamp replaced;

    @NotNull
    private String text;

    // TODO foreign key
    @NotNull
    private Integer user;

    //bi-directional many-to-one association to Shout
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "epoch", referencedColumnName = "epoch"),
            @JoinColumn(name = "id", referencedColumnName = "id")
    })
    private Shout shout;

    public ShoutRevision() {
    }

    public ShoutRevisionPK getId() {
        return this.id;
    }

    public void setId(ShoutRevisionPK id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return new Timestamp(this.date.getTime());
    }

    public void setDate(Timestamp date) {
        this.date = new Timestamp(date.getTime());
    }

    public Timestamp getReplaced() {
        return new Timestamp(this.replaced.getTime());
    }

    public void setReplaced(Timestamp replaced) {
        this.replaced = new Timestamp(replaced.getTime());
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUser() {
        return this.user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Shout getShout() {
        return this.shout;
    }

    public void setShout(Shout shout) {
        this.shout = shout;
    }

}