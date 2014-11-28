package at.rueckgr.chatbox.database.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "queries")
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
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "request", nullable = false)
    private Request request;

    @NotNull
    private Date date;

    @NotNull
    @Lob
    private String query;

    @NotNull
    @Lob
    private String parameters;

    @NotNull
    @Column(name = "execution_time")
    private Double executionTime;
}
