package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the smilies database table.
 */
@Entity
@Table(name = "smilies")
@NamedQueries({
        @NamedQuery(name = Smiley.QRY_FIND_ALL, query = "SELECT s FROM Smiley s"),
        @NamedQuery(name = Smiley.QRY_FIND_BY_FILENAME, query = "SELECT s FROM Smiley s WHERE s.filename = :filename"),
})
@Getter
@Setter
public class Smiley implements ChatboxEntity {
    private static final long serialVersionUID = 3953856754027689156L;

    public static final String QRY_FIND_ALL = "Smiley.findAll";
    public static final String QRY_FIND_BY_FILENAME = "Smiley.findByFilename";

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smilies_id_seq1")
    @SequenceGenerator(name = "smilies_id_seq1", sequenceName = "smilies_id_seq1")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "filename", nullable = false, length = 100)
    private String filename;

    @Column(name = "code", unique = true, length = 100)
    private String code;

    @Lob
    @Column(name = "meaning")
    private String meaning;

    public Smiley() {
    }

    public Smiley(String filename) {
        this.filename = filename;
    }
}
