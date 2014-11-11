package at.rueckgr.chatbox.database.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ShoutSmileysPK implements DatabaseThing, Serializable {
    private static final long serialVersionUID = 6860462509421869628L;

    @Column(name = "shout_id", nullable = false)
    private int shoutId;

    @Column(name = "shout_epoch", nullable = false)
    private int shoutEpoch;

    @Column(name = "smiley", nullable = false)
    private int smileyId;

    public ShoutSmileysPK() {
    }

    public ShoutSmileysPK(int shoutId, int shoutEpoch, int smileyId) {
        this.shoutId = shoutId;
        this.shoutEpoch = shoutEpoch;
        this.smileyId = smileyId;
    }
}
