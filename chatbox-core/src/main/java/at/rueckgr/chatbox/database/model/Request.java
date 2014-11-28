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
import java.util.Date;


/**
 * The persistent class for requests database table.
 */
@Entity
@Table(name = "requests")
@NamedQueries({
        @NamedQuery(name = Request.QRY_FIND_ALL, query = "SELECT r FROM Request r"),
})
@Getter
@Setter
public class Request implements ChatboxEntity {

    private static final long serialVersionUID = -62797957999265510L;

    public static final String QRY_FIND_ALL = "Request.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requests_id_seq1")
    @SequenceGenerator(name = "requests_id_seq1", sequenceName = "requests_id_seq1")
    private Integer id;

    @NotNull
    @Column(name = "timestamp")
    private Date date;

    @NotNull
    @Lob
    private String url;

    @NotNull
    @Lob
    private String ip;

    @Column(name = "request_time")
    private Double requestTime;

    @NotNull
    private String browser;

    private String username;
}
