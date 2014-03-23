package at.rueckgr.chatbox.database.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Entity
@Table(name = "shout_words")
public class ShoutWords implements Serializable, ChatboxEntity {

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

    public ShoutWords() {
    }

    public ShoutWords(Shout shout, Word word, int count) {
        this.shout = shout;
        this.word = word;

        this.id = new ShoutWordsPK(shout.getId().getId(), shout.getId().getEpoch(), word.getId(), count);
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
