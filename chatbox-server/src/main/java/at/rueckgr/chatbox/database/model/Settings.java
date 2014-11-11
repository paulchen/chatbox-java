package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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
}
