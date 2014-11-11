package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * The persistent class for the smilies database table.
 */
@Entity
@Table(name = "smilies")
@NamedQueries({
        @NamedQuery(name = Smiley.FIND_ALL, query = "SELECT s FROM Smiley s"),
        @NamedQuery(name = Smiley.FIND_BY_FILENAME, query = "SELECT s FROM Smiley s WHERE s.filename = :filename"),
})
@Getter
@Setter
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

    public Smiley() {
    }

    public Smiley(String filename) {
        this.filename = filename;
    }
}