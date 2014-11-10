package at.rueckgr.chatbox.database.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the shouts database table.
 */
@Entity
@Table(name = "shouts")
@NamedQueries({
    @NamedQuery(name = Shout.FIND_ALL, query = "SELECT s FROM Shout s"),
    @NamedQuery(name = Shout.FIND_LAST, query = "SELECT s FROM Shout s ORDER BY s.id.epoch DESC, s.id.id DESC")
})
public class Shout implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Shout.findAll";
    public static final String FIND_LAST = "Shout.findLast";

    @EmbeddedId
    private ShoutPK id;

    @NotNull
    @Column(name = "primary_id")
    private Integer primaryId;

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

    public Shout() {
    }

    public ShoutPK getId() {
        return this.id;
    }

    public void setId(ShoutPK id) {
        this.id = id;
    }

    public Date getDate() {
        return new Date(this.date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getHour() {
        return this.hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Integer primaryId) {
        this.primaryId = primaryId;
    }
}