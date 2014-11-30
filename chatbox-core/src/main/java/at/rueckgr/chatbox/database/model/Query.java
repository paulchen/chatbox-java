package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * The persistent class for queries database table.
 */
@Entity
@Table(name = "queries",
        indexes = {
                @Index(name = "queries_timestamp_idx", columnList = "timestamp")
        }
)
@NamedQueries({
        @NamedQuery(name = Query.QRY_FIND_ALL, query = "SELECT q FROM Query q"),
})
@Getter
@Setter
public class Query implements ChatboxEntity {

    private static final long serialVersionUID = -4972242691777936104L;

    public static final String QRY_FIND_ALL = "Query.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queries_id_seq1")
    @SequenceGenerator(name = "queries_id_seq1", sequenceName = "queries_id_seq1")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "request", nullable = false)
    private Request request;

    @NotNull
    @Column(name = "date", nullable = false)
    private Date date;

    @NotNull
    @Lob
    @Column(name = "query", nullable = false)
    private String query;

    @NotNull
    @Lob
    @Column(name = "parameters", nullable = false)
    private String parameters;

    @NotNull
    @Column(name = "execution_time", nullable = false)
    private Double executionTime;
}
