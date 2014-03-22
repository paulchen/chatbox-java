package at.rueckgr.chatbox.database.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the shouts database table.
 */
@Embeddable
public class ShoutPK implements DatabaseThing, Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer epoch;

    public ShoutPK() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEpoch() {
        return this.epoch;
    }

    public void setEpoch(Integer epoch) {
        this.epoch = epoch;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ShoutPK)) {
            return false;
        }
        ShoutPK castOther = (ShoutPK) other;
        return
                this.id.equals(castOther.id)
                        && this.epoch.equals(castOther.epoch);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.id.hashCode();
        hash = hash * prime + this.epoch.hashCode();

        return hash;
    }
}