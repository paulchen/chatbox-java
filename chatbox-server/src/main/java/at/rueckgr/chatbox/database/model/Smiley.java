package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the smilies database table.
 */
@Entity
@Table(name = "smilies")
@NamedQuery(name = "Smiley.findAll", query = "SELECT s FROM Smiley s")
public class Smiley implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    private String filename;

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

}