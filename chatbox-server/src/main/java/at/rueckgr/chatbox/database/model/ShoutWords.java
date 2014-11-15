package at.rueckgr.chatbox.database.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * @author paulchen
 */
@Entity
@Table(name = "shout_words",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "shout", "word" })
        }
)
@NamedQueries(
        @NamedQuery(name = ShoutWords.FIND_BY_SHOUT, query = "SELECT sw FROM ShoutWords sw WHERE sw.shout = :shout")
)
@Getter
@Setter
@EqualsAndHashCode(exclude = { "shout", "word", "count" })
public class ShoutWords implements Serializable, ChatboxEntity {

    public static final String FIND_BY_SHOUT = "ShoutWords.findByShout";

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "shout", referencedColumnName = "primary_id")
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
    }
}
