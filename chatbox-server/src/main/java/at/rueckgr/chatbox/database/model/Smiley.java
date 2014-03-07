package at.rueckgr.chatbox.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the smilies database table.
 */
@Entity
@Table(name = "smilies")
@NamedQueries({
        @NamedQuery(name = Smiley.FIND_ALL, query = "SELECT s FROM Smiley s"),
        @NamedQuery(name = Smiley.FIND_BY_FILENAME, query = "SELECT s FROM Smiley s WHERE s.filename = :filename"),
})
public class Smiley implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Smiley.findAll";
    public static final String FIND_BY_FILENAME = "Smiley.findByCode";

    @Id
    private Integer id;

    private String filename;

    @Column(unique = true)
    private String code;

    private String meaning;

    //bi-directional many-to-many association to Shout
    @ManyToMany(mappedBy = "smilies")
    private List<Shout> shouts;

    public Smiley() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<Shout> getShouts() {
        return this.shouts;
    }

    public void setShouts(List<Shout> shouts) {
        this.shouts = shouts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}