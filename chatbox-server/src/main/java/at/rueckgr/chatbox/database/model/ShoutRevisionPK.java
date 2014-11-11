package at.rueckgr.chatbox.database.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the shout_revisions database table.
 */
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ShoutRevisionPK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(insertable = false, updatable = false)
    private Integer id;

    @Column(insertable = false, updatable = false)
    private Integer epoch;

    private Integer revision;

    public ShoutRevisionPK() {
    }

    public ShoutRevisionPK(Integer id, Integer epoch, Integer revision) {
        this.id = id;
        this.epoch = epoch;
        this.revision = revision;
    }
}