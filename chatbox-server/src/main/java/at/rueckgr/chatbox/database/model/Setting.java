package at.rueckgr.chatbox.database.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the settings database table.
 * 
 */
@Entity
@Table(name="settings")
@NamedQuery(name="Setting.findAll", query="SELECT s FROM Setting s")
public class Setting implements Serializable, ChatboxEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String key;

	@NotNull
	private String value;

	public Setting() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}