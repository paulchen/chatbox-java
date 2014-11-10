package at.rueckgr.chatbox.database.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Entity
@Table(name = "shout_words")
@NamedQueries(
        @NamedQuery(name = ShoutWords.FIND_BY_SHOUT, query = "SELECT sw FROM ShoutWords sw WHERE sw.shout = :shout")
)
public class ShoutWords implements Serializable, ChatboxEntity {

    public static final String FIND_BY_SHOUT = "ShoutWords.findByShout";

    @EmbeddedId
    private ShoutWordsPK id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "shout_epoch", referencedColumnName = "epoch"),
            @JoinColumn(name = "shout_id", referencedColumnName = "id")
    })
    private Shout shout;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "word", referencedColumnName = "id")
    })
    private Word word;

    @Column(nullable = false)
    private int count;

    public ShoutWords() {
    }

    public ShoutWords(Shout shout, Word word, int count) {
        this.shout = shout;
        this.word = word;
        this.count = count;

        this.id = new ShoutWordsPK(shout.getId().getId(), shout.getId().getEpoch(), word.getId());
    }

    public ShoutWordsPK getId() {
        return id;
    }

    public void setId(ShoutWordsPK id) {
        this.id = id;
    }

    public Shout getShout() {
        return shout;
    }

    public void setShout(Shout shout) {
        this.shout = shout;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShoutWords that = (ShoutWords) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
