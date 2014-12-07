package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the settings database table.
 */
@Entity
@Table(name = "settings")
@NamedQuery(name = Settings.QRY_FIND_ALL, query = "SELECT s FROM Settings s")
@Getter
@Setter
public class Settings implements ChatboxEntity {
    private static final long serialVersionUID = 8091561855611916113L;

    public static final String QRY_FIND_ALL = "Settings.findAll";
    
    @Id
    @NotNull
    @Column(name = "key", nullable = false, length = 50)
    private String key;

    @NotNull
    @Lob
    @Column(name = "value", nullable = false)
    private String value;

    public Settings() {
    }

    public Settings(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
