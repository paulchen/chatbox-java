package at.rueckgr.chatbox.database.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Embeddable
public class ShoutWordsPK implements DatabaseThing, Serializable {
    private static final long serialVersionUID = 6860462509421869628L;

    @Column(name = "shout_id", nullable = false)
    private int shoutId;

    @Column(name = "shout_epoch", nullable = false)
    private int shoutEpoch;

    @Column(name = "word", nullable = false)
    private int wordId;

    public ShoutWordsPK() {
    }

    public ShoutWordsPK(int shoutId, int shoutEpoch, int wordId) {
        this.shoutId = shoutId;
        this.shoutEpoch = shoutEpoch;
        this.wordId = wordId;
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

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShoutWordsPK that = (ShoutWordsPK) o;

        if (shoutEpoch != that.shoutEpoch) {
            return false;
        }
        if (shoutId != that.shoutId) {
            return false;
        }
        if (wordId != that.wordId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = shoutId;
        result = 31 * result + shoutEpoch;
        result = 31 * result + wordId;
        return result;
    }
}
