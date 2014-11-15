package at.rueckgr.chatbox.database.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Entity
@Table(name = "shout_smilies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "shout", "smiley" })
        }
)
@NamedQueries(
        @NamedQuery(name = ShoutSmileys.FIND_BY_SHOUT, query = "SELECT sm FROM ShoutSmileys sm WHERE sm.shout = :shout")
)
@Getter
@Setter
@EqualsAndHashCode(exclude = { "shout", "smiley", "count" })
public class ShoutSmileys implements Serializable, ChatboxEntity {

    public static final String FIND_BY_SHOUT = "ShoutSmileys.findByShout";

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "shout", referencedColumnName = "primary_id")
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
    }
}
