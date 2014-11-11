package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the shout_revisions database table.
 */
@Entity
@Table(name = "shout_revisions")
@NamedQueries({
        @NamedQuery(name = ShoutRevision.QRY_FIND_ALL, query = "SELECT s FROM ShoutRevision s"),
        @NamedQuery(name = ShoutRevision.QRY_FIND_LATEST, query = "SELECT s FROM ShoutRevision s " +
                "WHERE s.id.id = :id AND s.id.epoch = :epoch ORDER BY s.id.revision DESC"),

})
@Getter
@Setter
public class ShoutRevision implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    public static final String QRY_FIND_ALL = "ShoutRevision.findAll";
    public static final String QRY_FIND_LATEST = "ShoutRevision.findLatest";

    @EmbeddedId
    private ShoutRevisionPK id;

    @NotNull
    private Date date;

    @NotNull
    private Date replaced;

    @NotNull
    private String text;

    // TODO foreign key
    @NotNull
    @Column(name = "user_id")
    private Integer user;

    @NotNull
    @Column(name = "primary_id")
    private Integer primaryId;

    //bi-directional many-to-one association to Shout
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "epoch", referencedColumnName = "epoch"),
            @JoinColumn(name = "id", referencedColumnName = "id")
    })
    private Shout shout;
}
