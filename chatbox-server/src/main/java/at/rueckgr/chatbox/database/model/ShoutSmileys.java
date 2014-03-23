package at.rueckgr.chatbox.database.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Entity
@Table(name = "shout_smilies")
public class ShoutSmileys implements Serializable, ChatboxEntity {

    @EmbeddedId
    private ShoutSmileysPK id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "shout_epoch", referencedColumnName = "epoch"),
            @JoinColumn(name = "shout_id", referencedColumnName = "id")
    })
    private Shout shout;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "smiley", referencedColumnName = "id")
    })
    private Smiley smiley;

    public ShoutSmileys() {
    }

    public ShoutSmileys(Shout shout, Smiley smiley, int count) {
        this.shout = shout;
        this.smiley = smiley;

        this.id = new ShoutSmileysPK(shout.getId().getId(), shout.getId().getEpoch(), smiley.getId(), count);
    }

    public ShoutSmileysPK getId() {
        return id;
    }

    public void setId(ShoutSmileysPK id) {
        this.id = id;
    }

    public Shout getShout() {
        return shout;
    }

    public void setShout(Shout shout) {
        this.shout = shout;
    }

    public Smiley getSmiley() {
        return smiley;
    }

    public void setSmiley(Smiley smiley) {
        this.smiley = smiley;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShoutSmileys that = (ShoutSmileys) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
