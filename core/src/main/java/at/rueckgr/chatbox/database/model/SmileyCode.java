package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "smiley_codes",
    indexes = {
            @Index(name = "smiley_codes_code_idx", columnList = "code")
    }
)
@NamedQueries({
        @NamedQuery(name = SmileyCode.FIND_BY_SMILEY,
                query = "SELECT sc FROM SmileyCode sc WHERE sc.smiley = :smiley ORDER BY sc.code ASC"
        )

})
@Getter
@Setter
public class SmileyCode {
    public static final String FIND_BY_SMILEY = "SmileyCode.findBySmiley";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smiley_codes_id_seq")
    @SequenceGenerator(name = "smiley_codes_id_seq", sequenceName = "smiley_codes_id_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "code", unique = true, length = 100, nullable = false)
    private String code;

    @NotNull
    @ManyToOne(optional = false)
    @Column(name = "smiley", nullable = false)
    private Smiley smiley;

    public SmileyCode() {
    }

    public SmileyCode(Smiley smiley, String code) {
        this.smiley = smiley;
        this.code = code;
    }
}
