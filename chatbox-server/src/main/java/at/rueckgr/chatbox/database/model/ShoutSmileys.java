package at.rueckgr.chatbox.database.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Entity
@Table(name = "shout_smilies")
@NamedQueries(
        @NamedQuery(name = ShoutSmileys.FIND_BY_SHOUT, query = "SELECT sm FROM ShoutSmileys sm WHERE sm.shout = :shout")
)
public class ShoutSmileys implements Serializable, ChatboxEntity {

    public static final String FIND_BY_SHOUT = "ShoutSmileys.findByShout";

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

    @Column(nullable = false)
    private int count;

    public ShoutSmileys() {
    }

    public ShoutSmileys(Shout shout, Smiley smiley, int count) {
        this.shout = shout;
        this.smiley = smiley;
        this.count = count;

        this.id = new ShoutSmileysPK(shout.getId().getId(), shout.getId().getEpoch(), smiley.getId());
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
