package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * The persistent class for the shout_revisions database table.
 */
@Entity
@Table(name = "shout_revisions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "shout", "revision" })
        },
        indexes = {
                @Index(name = "shout_revisions_shout_idx", columnList = "shout")
        }
)
@NamedQueries({
        @NamedQuery(name = ShoutRevision.QRY_FIND_ALL, query = "SELECT s FROM ShoutRevision s"),
        @NamedQuery(name = ShoutRevision.QRY_FIND_LATEST, query = "SELECT s FROM ShoutRevision s " +
                "WHERE s.shout.primaryId = :id ORDER BY s.revision DESC"),

})
@Getter
@Setter
public class ShoutRevision implements ChatboxEntity {
    private static final long serialVersionUID = 3249407758311648218L;

    public static final String QRY_FIND_ALL = "ShoutRevision.findAll";
    public static final String QRY_FIND_LATEST = "ShoutRevision.findLatest";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shout_revisions_id_seq")
    @SequenceGenerator(name = "shout_revisions_id_seq", sequenceName = "shout_revisions_id_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "revision", nullable = false)
    private Integer revision;

    @NotNull
    @Column(name = "date", nullable = false)
    private Date date;

    @NotNull
    @Column(name = "replaced", nullable = false)
    private Date replaced;

    @NotNull
    @Lob
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "shout", nullable = false)
    private Shout shout;
}
