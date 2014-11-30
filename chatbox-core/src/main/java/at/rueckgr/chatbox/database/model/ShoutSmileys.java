package at.rueckgr.chatbox.database.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author paulchen
 */
@Entity
@Table(name = "shout_smilies",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "shout", "smiley" })
        },
        indexes = {
                @Index(name = "shout_smilies_shout_idx", columnList = "shout"),
                @Index(name = "shout_smilies_smiley_idx", columnList = "smiley"),
        }
)
@NamedQueries(
        @NamedQuery(name = ShoutSmileys.QRY_FIND_BY_SHOUT, query = "SELECT sm FROM ShoutSmileys sm WHERE sm.shout = :shout")
)
@Getter
@Setter
@EqualsAndHashCode(exclude = { "shout", "smiley", "count" })
public class ShoutSmileys implements ChatboxEntity {
    private static final long serialVersionUID = -3741930808788765544L;

    public static final String QRY_FIND_BY_SHOUT = "ShoutSmileys.findByShout";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shout_smilies_id_seq")
    @SequenceGenerator(name = "shout_smilies_id_seq", sequenceName = "shout_smilies_id_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "shout", nullable = false)
    private Shout shout;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "smiley", nullable = false)
    private Smiley smiley;

    @NotNull
    @Column(name = "count", nullable = false)
    private int count;

    public ShoutSmileys() {
    }

    public ShoutSmileys(Shout shout, Smiley smiley, int count) {
        this.shout = shout;
        this.smiley = smiley;
        this.count = count;
    }
}
