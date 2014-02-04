package at.rueckgr.chatbox.database.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the shouts database table.
 * 
 */
@Entity
@Table(name="shouts")
@NamedQuery(name="Shout.findAll", query="SELECT s FROM Shout s")
public class Shout implements Serializable, ChatboxEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ShoutPK id;

	@NotNull
	private Timestamp date;

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
	@OneToMany(mappedBy="shout")
	private List<ShoutRevision> shoutRevisions;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user")
	private User userBean;

	//bi-directional many-to-many association to Smily
	@ManyToMany
	@JoinTable(
		name="shout_smilies"
		, joinColumns={
			@JoinColumn(name="shout_epoch", referencedColumnName="epoch"),
			@JoinColumn(name="shout_id", referencedColumnName="id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="smiley")
			}
		)
	private List<Smiley> smilies;

	//bi-directional many-to-many association to Word
	@ManyToMany
	@JoinTable(
		name="shout_words"
		, joinColumns={
			@JoinColumn(name="shout_epoch", referencedColumnName="epoch"),
			@JoinColumn(name="shout_id", referencedColumnName="id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="word", referencedColumnName="word")
			}
		)
	private List<Word> words;

	public Shout() {
	}

	public ShoutPK getId() {
		return this.id;
	}

	public void setId(ShoutPK id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
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

	public User getUserBean() {
		return this.userBean;
	}

	public void setUserBean(User userBean) {
		this.userBean = userBean;
	}

	public List<Smiley> getSmilies() {
		return this.smilies;
	}

	public void setSmilies(List<Smiley> smilies) {
		this.smilies = smilies;
	}

	public List<Word> getWords() {
		return this.words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

}