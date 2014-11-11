package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * The persistent class for the words database table.
 */
@Entity
@Table(name = "words")
@NamedQueries({
    @NamedQuery(name = Word.FIND_ALL, query = "SELECT w FROM Word w"),
    @NamedQuery(name = Word.FIND_BY_WORD, query = "SELECT w FROM Word w WHERE w.word = :word"),
})
@Getter
@Setter
public class Word implements Serializable, ChatboxEntity {
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Word.findAll";
    public static final String FIND_BY_WORD = "Word.findByWord";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "words_id_seq1")
    @SequenceGenerator(name = "words_id_seq1", sequenceName = "words_id_seq1")
    private Integer id;

    @NotNull
    private String word;

    public Word() {
    }

    public Word(String word) {
        this.word = word;
    }
}
