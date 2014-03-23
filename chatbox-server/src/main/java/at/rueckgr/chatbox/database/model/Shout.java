package at.rueckgr.chatbox.database.model;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


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
    private Date date;

    @NotNull
    private Integer day;

    @NotNull
    private Boolean deleted;

    @NotNull
    private Integer hour;

    @NotNull
    private String message;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

    //bi-directional many-to-one association to ShoutRevision
    @OneToMany(mappedBy = "shout")
    private List<ShoutRevision> shoutRevisions;

    //bi-directional many-to-one association to User
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shout")
    private List<ShoutSmileys> smilies;

    @OneToMany(mappedBy = "shout")
    private List<ShoutWords> words;

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

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
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

    public List<ShoutRevision> getShoutRevisions() {
        return this.shoutRevisions;
    }

    public void setShoutRevisions(List<ShoutRevision> shoutRevisions) {
        this.shoutRevisions = shoutRevisions;
    }

    public ShoutRevision addShoutRevision(ShoutRevision shoutRevision) {
        getShoutRevisions().add(shoutRevision);
        shoutRevision.setShout(this);

        return shoutRevision;
    }

    public ShoutRevision removeShoutRevision(ShoutRevision shoutRevision) {
        getShoutRevisions().remove(shoutRevision);
        shoutRevision.setShout(null);

        return shoutRevision;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ShoutSmileys> getSmilies() {
        return smilies;
    }

    public void setSmilies(List<ShoutSmileys> smilies) {
        this.smilies = smilies;
    }

    public List<ShoutWords> getWords() {
        return words;
    }

    public void setWords(List<ShoutWords> words) {
        this.words = words;
    }
}