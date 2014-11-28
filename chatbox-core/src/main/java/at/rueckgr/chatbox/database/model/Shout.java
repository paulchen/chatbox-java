package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * The persistent class for the shouts database table.
 */
@Entity
@Table(name = "shouts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "id", "epoch" })
        },
        indexes = {
                @Index(name = "shouts_id_epoch_idx", columnList = "id, epoch"),
                @Index(name = "shouts_day_idx", columnList = "day"),
                @Index(name = "shouts_deleted_idx", columnList = "deleted"),
                @Index(name = "shouts_hour_idx", columnList = "hour"),
                @Index(name = "shouts_month_idx", columnList = "month"),
                @Index(name = "shouts_year_idx", columnList = "year"),
                @Index(name = "shouts_user_idx", columnList = "user_id"),
        }
)
@NamedQueries({
    @NamedQuery(name = Shout.QRY_FIND_ALL, query = "SELECT s FROM Shout s"),
    @NamedQuery(name = Shout.QRY_FIND_LAST, query = "SELECT s FROM Shout s ORDER BY s.epoch DESC, s.id DESC")
})
@Getter
@Setter
public class Shout implements ChatboxEntity {
    private static final long serialVersionUID = -2072701453185873070L;

    public static final String QRY_FIND_ALL = "Shout.findAll";
    public static final String QRY_FIND_LAST = "Shout.findLast";

    @NotNull
    @Column(name = "primary_id", nullable = false)
    @Id
    private Integer primaryId;

    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "epoch", nullable = false)
    private Integer epoch;

    @NotNull
    @Column(name = "date", nullable = false)
    private Date date;

    @NotNull
    @Column(name = "day", nullable = false)
    private Integer day;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @NotNull
    @Column(name = "hour", nullable = false)
    private Integer hour;

    @NotNull
    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "month", nullable = false)
    private Integer month;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
