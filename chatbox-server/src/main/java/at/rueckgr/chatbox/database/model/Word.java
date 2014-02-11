package at.rueckgr.chatbox.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the words database table.
 */
@Entity
@Table(name = "words")
@NamedQuery(name = "Word.findAll", query = "SELECT w FROM Word w")
public class Word implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @NotNull
    private String word;

    //bi-directional many-to-many association to Shout
    @ManyToMany(mappedBy = "words")
    private List<Shout> shouts;

    public Word() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Shout> getShouts() {
        return this.shouts;
    }

    public void setShouts(List<Shout> shouts) {
        this.shouts = shouts;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}