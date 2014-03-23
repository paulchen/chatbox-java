package at.rueckgr.chatbox.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smilies_id_seq1")
    @SequenceGenerator(name = "smilies_id_seq1", sequenceName = "smilies_id_seq1")
    private Integer id;

    private String filename;

    @Column(unique = true)
    private String code;

    private String meaning;

    @OneToMany(mappedBy = "smiley")
    private List<ShoutSmileys> shoutSmileys;

    public Smiley() {
    }

    public Smiley(String filename) {
        this.filename = filename;
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

    public List<ShoutSmileys> getShoutSmileys() {
        return shoutSmileys;
    }

    public void setShoutSmileys(List<ShoutSmileys> shoutSmileys) {
        this.shoutSmileys = shoutSmileys;
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