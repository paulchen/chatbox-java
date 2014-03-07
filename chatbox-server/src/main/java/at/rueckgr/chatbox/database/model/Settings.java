package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * The persistent class for the settings database table.
 */
@Entity
@Table(name = "settings")
@NamedQuery(name = "Settings.findAll", query = "SELECT s FROM Settings s")
public class Settings implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    public static final String FORUM_USERNAME = "forum_username";
    public static final String FORUM_PASSWORD = "forum_password";

    @Id
    private String key;

    @NotNull
    private String value;

    public Settings() {
    }

    public Settings(String key, String value) {
        this.key = key;
        this.value = value;
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