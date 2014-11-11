package at.rueckgr.chatbox.database.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the shouts database table.
 */
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ShoutPK implements DatabaseThing, Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer epoch;
}