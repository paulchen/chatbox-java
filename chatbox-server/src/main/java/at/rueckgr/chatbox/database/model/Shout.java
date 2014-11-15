package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the shouts database table.
 */
@Entity
@Table(name = "shouts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "id", "epoch" })
        }
)
@NamedQueries({
    @NamedQuery(name = Shout.FIND_ALL, query = "SELECT s FROM Shout s"),
    @NamedQuery(name = Shout.FIND_LAST, query = "SELECT s FROM Shout s ORDER BY s.epoch DESC, s.id DESC")
})
@Getter
@Setter
public class Shout implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Shout.findAll";
    public static final String FIND_LAST = "Shout.findLast";

    @NotNull
    @Column(name = "primary_id")
    @Id
    private Integer primaryId;

    @NotNull
    private Integer id;

    @NotNull
    private Integer epoch;

    @NotNull
    private Date date;

    @NotNull
    private Integer day;

    @NotNull
    // TODO this is a actually a Boolean
    private Integer deleted;

    @NotNull
    private Integer hour;

    @NotNull
    private String message;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

    //bi-directional many-to-one association to User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
