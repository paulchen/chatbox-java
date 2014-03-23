package at.rueckgr.chatbox.database.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Embeddable
public class ShoutSmileysPK implements DatabaseThing, Serializable {
    private static final long serialVersionUID = 6860462509421869628L;

    @Column(name = "shout_id", nullable = false)
    private int shoutId;

    @Column(name = "shout_epoch", nullable = false)
    private int shoutEpoch;

    @Column(name = "smiley", nullable = false)
    private int smileyId;

    @Column(nullable = false)
    private int count;

    public ShoutSmileysPK() {
    }

    public ShoutSmileysPK(int shoutId, int shoutEpoch, int smileyId, int count) {
        this.shoutId = shoutId;
        this.shoutEpoch = shoutEpoch;
        this.smileyId = smileyId;
        this.count = count;
    }

    public int getShoutId() {
        return shoutId;
    }

    public void setShoutId(int shoutId) {
        this.shoutId = shoutId;
    }

    public int getShoutEpoch() {
        return shoutEpoch;
    }

    public void setShoutEpoch(int shoutEpoch) {
        this.shoutEpoch = shoutEpoch;
    }

    public int getSmileyId() {
        return smileyId;
    }

    public void setSmileyId(int smileyId) {
        this.smileyId = smileyId;
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

        ShoutSmileysPK that = (ShoutSmileysPK) o;

        if (count != that.count) {
            return false;
        }
        if (shoutEpoch != that.shoutEpoch) {
            return false;
        }
        if (shoutId != that.shoutId) {
            return false;
        }
        if (smileyId != that.smileyId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = shoutId;
        result = 31 * result + shoutEpoch;
        result = 31 * result + smileyId;
        result = 31 * result + count;
        return result;
    }
}
