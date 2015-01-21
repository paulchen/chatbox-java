package at.rueckgr.chatbox.database.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "archive_pages_to_refetch",
        indexes = {
                @Index(name = "archive_pages_to_refetch_idx", columnList = "done")
        }
)
@NamedQueries({
        @NamedQuery(name = ArchivePagesToRefetch.QRY_FIND_OPEN, query = "SELECT a FROM ArchivePagesToRefetch a WHERE a.done IS NULL ORDER BY a.added ASC")
})
@Getter
@Setter
public class ArchivePagesToRefetch implements ChatboxEntity {

    private static final long serialVersionUID = -1390133186093028519L;

    public static final String QRY_FIND_OPEN = "ArchivePagesToRefetch.findOpen";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "archive_pages_to_refetch_id_seq")
    @SequenceGenerator(name = "archive_pages_to_refetch_id_seq", sequenceName = "archive_pages_to_refetch_id_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "page", nullable = false)
    private Integer page;

    @NotNull
    @Column(name = "added", nullable = false, updatable = false)
    private Date added;

    @Column(name = "done")
    private Date done;
}
