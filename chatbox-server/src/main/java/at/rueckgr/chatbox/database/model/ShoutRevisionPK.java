package at.rueckgr.chatbox.database.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the shout_revisions database table.
 * 
 */
@Embeddable
public class ShoutRevisionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private Integer id;

	@Column(insertable=false, updatable=false)
	private Integer epoch;

	private Integer revision;

	public ShoutRevisionPK() {
	}
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEpoch() {
		return this.epoch;
	}
	public void setEpoch(Integer epoch) {
		this.epoch = epoch;
	}
	public Integer getRevision() {
		return this.revision;
	}
	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ShoutRevisionPK)) {
			return false;
		}
		ShoutRevisionPK castOther = (ShoutRevisionPK)other;
		return 
			this.id.equals(castOther.id)
			&& this.epoch.equals(castOther.epoch)
			&& this.revision.equals(castOther.revision);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id.hashCode();
		hash = hash * prime + this.epoch.hashCode();
		hash = hash * prime + this.revision.hashCode();
		
		return hash;
	}
}